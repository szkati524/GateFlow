package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.CarModelAssembler;
import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.service.CarService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final CarService carService;
    private final CarModelAssembler assembler;

    public CarController(CarRepository carRepository, CompanyRepository companyRepository, CarService carService, CarModelAssembler assembler) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
        this.carService = carService;
        this.assembler = assembler;
    }


    @GetMapping("/registration/{registrationNumber}")
    public CarDto findByRegistration(@PathVariable String registrationNumber){
        return carRepository.findByRegistrationNumberIgnoreCase(registrationNumber)
                .map(assembler::toModel)
                .orElseThrow(() -> new RuntimeException("nie znaleziono auta o podanej rejestracji: " + registrationNumber));

    }
    @GetMapping
    public CollectionModel<CarDto> getAllCars(){
     return assembler.toCollectionModel(carRepository.findAll());
    }
    @GetMapping("/{id}")
    public CarDto showCar(@PathVariable Long id){
       return carRepository.findById(id)
               .map(assembler::toModel)
               .orElseThrow(() -> new RuntimeException("nie znaleziono auta o ID: " + id));
    }
    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable Long id, @RequestBody Car carRequest){
                Car updatedCar = carService.updatedCar(id,carRequest);
                return assembler.toModel(updatedCar);
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody Car car){
       Car savedCar =  carRepository.save(car);
        CarDto dto = assembler.toModel(savedCar);
        return ResponseEntity.created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);
    }
@DeleteMapping("/{id}")
        public ResponseEntity<?> deleteCar(@PathVariable Long id) {
    carService.deleteCar(id);
    return ResponseEntity.noContent().build();

}
}
