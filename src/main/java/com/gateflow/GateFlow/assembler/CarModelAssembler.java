package com.gateflow.GateFlow.assembler;

import com.gateflow.GateFlow.controller.CarController;
import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.model.Car;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CarModelAssembler extends RepresentationModelAssemblerSupport<Car, CarDto> {
    public CarModelAssembler() {
        super(CarController.class, CarDto.class);
    }

    @Override
    public CarDto toModel(Car car) {
       CarDto dto = createModelWithId(car.getId(),car);
       dto.setId(car.getId());
       dto.setRegistrationNumber(car.getRegistrationNumber());
       dto.setBrand(car.getBrand());
       dto.setCompanyName(car.getCompany() != null ? car.getCompany().getName() : "Brak przypisanej nazwy firmy!");
       dto.setActive(car.isActive());
       return dto;
    }

    @Override
    public CollectionModel<CarDto> toCollectionModel(Iterable<? extends Car> entities) {
        CollectionModel<CarDto> carModels = super.toCollectionModel(entities);
        carModels.add(linkTo(methodOn(CarController.class).getAllCars()).withSelfRel());
        return carModels;
    }
}
