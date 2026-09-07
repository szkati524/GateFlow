package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.CompanyCreateDto;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Visit;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;

@Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;

}
public List<CompanyDto>  findAll(){
    return companyRepository.findAll().stream()
            .map(c -> new CompanyDto(c.getId(),c.getName()))
            .toList();
}
public CompanyDto findById(Long id){
    return companyRepository.findById(id)
            .map(c -> new CompanyDto(c.getId(),c.getName()))
            .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID: " + id));

}
@Transactional
    public CompanyDto addCompany(CompanyCreateDto dto){
    companyRepository.findByNameIgnoreCase(dto.name())
            .ifPresent(existing -> {
                throw new RuntimeException("Firma o nazwie " + dto.name() + " istnieje");
            });
    Company company = new Company();
    company.setName(dto.name());
    Company saved = companyRepository.save(company);
return new CompanyDto(saved.getId(),saved.getName() );
    }
@Transactional
    public void deleteCompany(Long id){
     if (!companyRepository.existsById(id)){
         throw new RuntimeException("Nie znaleziono firmy o ID: " + id);
     }
     companyRepository.deleteById(id);
    }

    @Transactional
    public CompanyDto updateCompany(Long id, CompanyCreateDto dto){
   Company company = companyRepository.findById(id)
           .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID: " + id));
   company.setName(dto.name());
   return new CompanyDto(company.getId(),company.getName());
    }


}
