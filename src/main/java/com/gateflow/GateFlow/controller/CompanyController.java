package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.service.CompanyService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }
@GetMapping("{id}")
public EntityModel<Company> showCompany(@PathVariable Long id){
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Nie znaleziono takiej firmy"));
       EntityModel model =  EntityModel.of(company,
                linkTo(methodOn(CompanyController.class).showCompany(company.getId())).withSelfRel());
    linkTo(methodOn(CompanyController.class).getAllCompanies()).withRel("all-companies");
        return model;
}
    @GetMapping
public CollectionModel<EntityModel<Company>> getAllCompanies (){
        List<EntityModel<Company>> companies = companyRepository.findAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CompanyController.class).showCompany(c.getId())).withSelfRel()))
                        .collect(Collectors.toList());
               return CollectionModel.of(companies,linkTo(methodOn(CompanyController.class).getAllCompanies()).withSelfRel());
    }
}
