package com.gateflow.GateFlow.controller;


import com.gateflow.GateFlow.assembler.VisitModelAssembler;
import com.gateflow.GateFlow.dto.CompanyCreateDto;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import com.gateflow.GateFlow.service.CompanyService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
  private final CompanyService companyService;


    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;


    }


    @GetMapping
public List<CompanyDto> getAllCompanies (){
        return companyService.findAll();
    }
    @GetMapping("/{id}")
    public CompanyDto getCompanyById(@PathVariable Long id){
        return companyService.findById(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDto addCompany(@RequestBody CompanyCreateDto dto) {
        return companyService.addCompany(dto);
    }

    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id, @RequestBody CompanyCreateDto dto) {
        return companyService.updateCompany(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }
}
