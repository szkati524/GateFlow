package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.Company;
import com.gateflow.GateFlow.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
@Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public void addCompany(Company company){
    companyRepository.save(company  );

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
    public Company findByCompanyName(String name){
    return  companyRepository.findByNameIgnoreCase(name);
    }
}
