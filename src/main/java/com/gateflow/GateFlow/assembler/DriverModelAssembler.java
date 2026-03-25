package com.gateflow.GateFlow.assembler;

import com.gateflow.GateFlow.controller.CompanyController;
import com.gateflow.GateFlow.controller.DriverController;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.dto.DriverDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Driver;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DriverModelAssembler extends RepresentationModelAssemblerSupport<Driver, DriverDto> {
    public DriverModelAssembler() {
        super(DriverController.class, DriverDto.class);
    }

    @Override
    public DriverDto toModel(Driver driver) {
        DriverDto dto = createModelWithId(driver.getId(),driver);
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setSurname(driver.getSurname());
        if(driver.getCompany() != null) {
            dto.setCompanyName(driver.getCompany().getName());
        }
        dto.add(linkTo(methodOn(DriverController.class).getAllDrivers()).withRel("all-drivers"));
        return dto;
    }
    @Override
    public CollectionModel<DriverDto> toCollectionModel(Iterable<? extends Driver> entities) {
        CollectionModel<DriverDto> driverModels = super.toCollectionModel(entities);
        driverModels.add(linkTo(methodOn(DriverController.class).getAllDrivers()).withSelfRel());
        return driverModels;
    }
}
