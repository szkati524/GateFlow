package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.CompanyModelAssembler;
import com.gateflow.GateFlow.assembler.VisitModelAssembler;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Visit;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
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
  private final CompanyRepository companyRepository;
  private VisitRepository visitRepository;
  private CompanyModelAssembler companyAssembler;
  private VisitModelAssembler visitAssembler;

    public CompanyController(CompanyRepository companyRepository, VisitRepository visitRepository, CompanyModelAssembler companyAssembler, VisitModelAssembler visitAssembler) {
        this.companyRepository = companyRepository;
        this.visitRepository = visitRepository;
        this.companyAssembler = companyAssembler;
        this.visitAssembler = visitAssembler;
    }


    @GetMapping("/{id}")
public CompanyDto showCompany(@PathVariable Long id){
        return companyRepository.findById(id)
                .map(companyAssembler::toModel)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID: " + id) );
}
    @GetMapping
public CollectionModel<CompanyDto> getAllCompanies (){
        return companyAssembler.toCollectionModel(companyRepository.findAll());
    }
    @PutMapping("/{id}")
    public CompanyDto updateCompany(@PathVariable Long id,@RequestBody Company companyRequest){
        Company updatedCompany = companyRepository.findById(id)
                .map(companyExisting -> {
                    if (companyRequest.getName() != null) {
                        companyExisting.setName(companyRequest.getName());
                    }
                    return companyRepository.save(companyExisting);
                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o podanym Id" + id)   );
        return companyAssembler.toModel(updatedCompany);

    }
    @GetMapping("/{id}/history")
    public CollectionModel<VisitDto> showVisits(@PathVariable Long id){
      if(!companyRepository.existsById(id)){
          throw new RuntimeException("Nie znaleziono firmy o ID: " + id);
      }
      return visitAssembler.toCollectionModel(visitRepository.findByCompanyId(id));
    }

}
