package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final VisitRepository visitRepository;
@Autowired
    public CompanyService(CompanyRepository companyRepository, VisitRepository visitRepository) {
        this.companyRepository = companyRepository;
    this.visitRepository = visitRepository;
}
@Transactional
    public Company addCompany(CompanyDto dto){
    companyRepository.findByNameIgnoreCase(dto.getName())
            .ifPresent(existing -> {
                throw new RuntimeException("Firma o nazwie " + dto.getName() + " istnieje");
            });
    Company newCompany = new Company();
    newCompany.setName(dto.getName());
return companyRepository.save(newCompany);
    }
    public List<Company> findAllCompany(){
    return companyRepository.findAll();
    }
    public Optional<Company> findById(Long id){
    return companyRepository.findById(id);
    }
    public void deleteCompanyById(Long id){
     companyRepository.deleteById(id);
    }
    public Optional<Company> findByCompanyName(String name){
    return  companyRepository.findByNameIgnoreCase(name);
    }
    @Transactional
    public Company updatedCompany(Long id,Company request){
    return companyRepository.findById(id)
            .map(existing -> {
                if (request.getName() != null) existing.setName(request.getName());
                return companyRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID: " + id) );
    }
}
