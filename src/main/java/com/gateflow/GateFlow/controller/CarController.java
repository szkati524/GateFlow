package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.CarModelAssembler;
import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final CarModelAssembler assembler;

    public CarController(CarRepository carRepository, CompanyRepository companyRepository, CarModelAssembler assembler) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
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
               .orElseThrow(() -> new RuntimeException("nie znaleziono auta o podanym id"));
    }
    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable Long id, @RequestBody Car carRequest){
                 Car updatedCar = carRepository.findById(id)
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
                    return carRepository.save(carExisting);
                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono auta o ID " + id));
                 return assembler.toModel(updatedCar);
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody Car car){
       Car savedCar =  carRepository.save(car);
        CarDto dto = assembler.toModel(savedCar);
        return ResponseEntity.created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);
    }
@DeleteMapping("deletecar/{id}")
        public ResponseEntity<?> deleteCar(@PathVariable Long id){
        return carRepository.findById(id)
                .map(car -> {
                    carRepository.delete(car);
                  return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
}
}
