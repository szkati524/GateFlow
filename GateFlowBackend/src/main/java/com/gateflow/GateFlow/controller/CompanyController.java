package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.CompanyModelAssembler;
import com.gateflow.GateFlow.assembler.VisitModelAssembler;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import com.gateflow.GateFlow.service.CompanyService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
  private final CompanyService companyService;
  private CompanyModelAssembler companyAssembler;
  private VisitModelAssembler visitAssembler;

    public CompanyController(CompanyService companyService,  CompanyModelAssembler companyAssembler, VisitModelAssembler visitAssembler) {
        this.companyService = companyService;

        this.companyAssembler = companyAssembler;
        this.visitAssembler = visitAssembler;
    }

    @PostMapping
    public ResponseEntity<CompanyDto> addCompany(@RequestBody CompanyDto companyDto){
        Company savedCompany = companyService.addCompany(companyDto);
        CompanyDto dto = companyAssembler.toModel(savedCompany);
        return ResponseEntity
                .created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);
    }


    @GetMapping("/{id}")
public CompanyDto showCompany(@PathVariable Long id){
        return companyService.findById(id)
                .map(companyAssembler::toModel)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID: " + id) );
}
    @GetMapping
public CollectionModel<CompanyDto> getAllCompanies (){
        return companyAssembler.toCollectionModel(companyService.findAllCompany());
    }
    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id,@RequestBody Company companyRequest){
        Company updatedCompany = companyService.updatedCompany(id,companyRequest);
        return companyAssembler.toModel(updatedCompany);


    }
    @GetMapping("/{id}/history")
    public CollectionModel<VisitDto> showVisits(@PathVariable Long id){
      if(!companyService.existsById(id)){
          throw new RuntimeException("Nie znaleziono firmy o ID: " + id);
      }
      return visitAssembler.toCollectionModel(companyService.findVisitsByCompanyId(id));
    }

}
