package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Visit;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import com.gateflow.GateFlow.service.CompanyService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final VisitRepository visitRepository;

    public CompanyController(CompanyService companyService, CompanyRepository companyRepository, VisitRepository visitRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.visitRepository = visitRepository;
    }
@GetMapping("/{id}")
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
    @PutMapping("/{id}")
    public EntityModel<Company> updateCompany(@PathVariable Long id,@RequestBody Company companyRequest){
        return companyRepository.findById(id)
                .map(companyExisting -> {
                    if (companyRequest.getName() != null) {
                        companyExisting.setName(companyRequest.getName());
                    }

                    Company updatedCompany = companyRepository.save(companyExisting);
                    return EntityModel.of(updatedCompany,linkTo(methodOn(CompanyController.class).showCompany(id)).withSelfRel(),
                            linkTo(methodOn(CompanyController.class).getAllCompanies()).withRel("all-companies"));

                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o podanym Id" + id)   );

    }
    @GetMapping("/{id}/history")
    public CollectionModel<EntityModel<Visit>> showVisits(@PathVariable Long id){
       Company company = companyRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID " + id));
       List<EntityModel<Visit>> visits = visitRepository.findByCompanyId(id).stream()
               .map(visit -> EntityModel.of(visit,
                       linkTo(methodOn(VisitController.class).showVisit(visit.getId())).withSelfRel()))
               .collect(Collectors.toList());
       return CollectionModel.of(visits,
               linkTo(methodOn(CompanyController.class).showVisits(id)).withSelfRel(),
       linkTo(methodOn(CompanyController.class).getAllCompanies()).withRel("all-companies"));
    }

}
