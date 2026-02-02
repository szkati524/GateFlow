package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.service.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarRepository carRepository;
    private final CarService carService;
    private final CompanyRepository companyRepository;

    public CarController(CarRepository carRepository, CarService carService, CompanyRepository companyRepository) {
        this.carRepository = carRepository;
        this.carService = carService;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/{registrationNumber}")
    public EntityModel<Car> findByRegistration(@PathVariable String registrationNumber){
        Car car = carRepository.findByRegistrationNumberIgnoreCase(registrationNumber).orElseThrow(() -> new RuntimeException("Nie znaleziono takiego auta: " + registrationNumber));
        EntityModel<Car> model = EntityModel.of(car);
        model.add(linkTo(methodOn(CarController.class).findByRegistration(registrationNumber)).withSelfRel());
        model.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("all-cars"));
        return model;
    }
    @GetMapping
    public CollectionModel<EntityModel<Car>> getAllCars(){
        List<EntityModel<Car>> cars = carRepository.findAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CarController.class).findByRegistration(c.getRegistrationNumber())).withSelfRel())).toList();
        return CollectionModel.of(cars,linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
    }
    @GetMapping("/{id}")
    public EntityModel<Car> showCar(@PathVariable Long id){
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono takiego auta: ")    );
        EntityModel<Car> model = EntityModel.of(car);
        model.add(linkTo(methodOn(CarController.class).showCar(id)).withSelfRel());
        model.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("all-cars"));
        return model;
    }
    @PutMapping("/{id}")
    public EntityModel<Car> updateCar(@PathVariable Long id, @RequestBody Car carRequest){
        return carRepository.findById(id)
                .map(carExisting -> {

                    if (carRequest.getRegistrationNumber() != null) {
                        carExisting.setRegistrationNumber(carRequest.getRegistrationNumber());
                    }
                    if (carRequest.getBrand() != null) {
                        carExisting.setBrand(carRequest.getBrand());
                    }


                    if (carRequest.getCompany() != null && carRequest.getCompany().getName() != null) {
                        Company company = companyRepository.findByNameIgnoreCase(carRequest.getCompany().getName())
                                .orElseGet(() -> companyRepository.save(carRequest.getCompany()));
                        carExisting.setCompany(company);
                    }

                    Car updatedCar = carRepository.save(carExisting);
                    return EntityModel.of(updatedCar,
                            linkTo(methodOn(CarController.class).showCar(updatedCar.getId())).withSelfRel());
                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono auta o ID " + id));
    }

    @PostMapping("/addcar")
    public EntityModel<Car> addCar(@RequestBody Car car){
       Car savedCar =  carRepository.save(car);
        return EntityModel.of(savedCar,
                linkTo(methodOn(CarController.class).showCar(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).getAllCars()).withRel("all-cars")  );
    }
@DeleteMapping("deletecar/{id}")
        public ResponseEntity<?> deleteCar(@PathVariable Long id){
        return carRepository.findById(id)
                .map(car -> {
                    carRepository.delete(car);
                    var link = linkTo(methodOn(CarController.class).getAllCars()).withRel("all-cars");
                    return ResponseEntity.ok(CollectionModel.empty(link));
                })
                .orElse(ResponseEntity.notFound().build());
}
}
