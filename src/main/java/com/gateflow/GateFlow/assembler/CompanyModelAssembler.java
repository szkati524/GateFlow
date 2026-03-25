package com.gateflow.GateFlow.assembler;

import com.gateflow.GateFlow.controller.CarController;
import com.gateflow.GateFlow.controller.CompanyController;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.model.Company;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class CompanyModelAssembler extends RepresentationModelAssemblerSupport<Company, CompanyDto> {

    public CompanyModelAssembler() {
        super(CompanyController.class, CompanyDto.class);
    }

    @Override
    public CompanyDto toModel(Company company) {
        CompanyDto dto = createModelWithId(company.getId(),company);
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.add(linkTo(methodOn(CarController.class).getAllCars()).withRel("company-cars"));
        return dto;
    }
    @Override
    public CollectionModel<CompanyDto> toCollectionModel(Iterable<? extends Company> entities) {
        CollectionModel<CompanyDto> companyModels = super.toCollectionModel(entities);
        companyModels.add(linkTo(methodOn(CompanyController.class)).withSelfRel());
        return companyModels;
    }
}
