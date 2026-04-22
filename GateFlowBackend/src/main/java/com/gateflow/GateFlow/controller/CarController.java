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

    private final CarService carService;
    private final CarModelAssembler assembler;

    public CarController( CarService carService, CarModelAssembler assembler) {

        this.carService = carService;
        this.assembler = assembler;
    }


    @GetMapping("/registration/{registrationNumber}")
    public CarDto findByRegistration(@PathVariable String registrationNumber){
        return carService.findByRegistrationNumber(registrationNumber)
                .map(assembler::toModel)
                .orElseThrow(() -> new RuntimeException("nie znaleziono auta o podanej rejestracji: " + registrationNumber));

    }
    @GetMapping
    public CollectionModel<CarDto> getAllCars(){
     return assembler.toCollectionModel(carService.findAll());
    }
    @GetMapping("/{id}")
    public CarDto showCar(@PathVariable Long id){
       return carService.findById(id)
               .map(assembler::toModel)
               .orElseThrow(() -> new RuntimeException("nie znaleziono auta o ID: " + id));
    }
    @PutMapping("/{id}")
    public CarDto updateCar(@PathVariable Long id, @RequestBody CarDto requestDto) {
        Car updatedCar = carService.updatedCar(id, requestDto);
        return assembler.toModel(updatedCar);
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody CarDto carDto){
       Car savedCar =  carService.addCar(carDto);
        CarDto dto = assembler.toModel(savedCar);
        return ResponseEntity.created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);
    }
@DeleteMapping("/{id}")
        public ResponseEntity<?> deleteCar(@PathVariable Long id) {
    carService.deleteCar(id);
    return ResponseEntity.noContent().build();

}
}
