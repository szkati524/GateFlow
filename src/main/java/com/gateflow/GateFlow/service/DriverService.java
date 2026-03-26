package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.DriverRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final CompanyRepository companyRepository;
@Autowired
    public DriverService(DriverRepository driverRepository, CompanyRepository companyRepository) {
        this.driverRepository = driverRepository;
    this.companyRepository = companyRepository;
}
    public void addDriver(Driver driver){
        driverRepository.save(driver);
    }
    public Optional<Driver> findById(Long id){
        return driverRepository.findById(id);
    }
    public List<Driver> findAllDrivers(){
        return driverRepository.findAll();
    }
    public Optional<Driver> findByFullName(String name,String surname){
        return driverRepository.findByNameAndSurnameIgnoreCase(name,surname);
    }
    public void deleteById(Long id){
        driverRepository.deleteById(id);
    }
    public List<Driver> findByCompany(String name){
       return driverRepository.findByCompanyNameIgnoreCase(name);
    }
    public List<Driver> findByCompanyId(Long id){
        return driverRepository.findByCompanyId(id);
    }
    @Transactional
    public Driver updateDriver(Long id,Driver request){
    return driverRepository.findById(id)
            .map(existing -> {
                if (request.getName() != null ) existing.setName(request.getName());
                if (request.getSurname() != null) existing.setSurname(request.getSurname());
                if (request.getCompany() != null)
                handleCompanyAssignment(request);
                existing.setCompany(request.getCompany());
                {
                   return driverRepository.save(existing);
                }
            })
            .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o ID: " + id)  );
    }

    private void handleCompanyAssignment(Driver driver) {
        Company companyRequest = driver.getCompany();
        if (companyRequest == null) return;

        Company finalCompany;

        if (companyRequest.getId() != null) {
            finalCompany = companyRepository.findById(companyRequest.getId())
                    .orElseThrow(() -> new RuntimeException("Nie znaleziono firmy o ID: " + companyRequest.getId()));
        }

        else if (companyRequest.getName() != null && !companyRequest.getName().isBlank()) {
            finalCompany = companyRepository.findByNameIgnoreCase(companyRequest.getName())
                    .orElseGet(() -> {

                        Company newCompany = new Company();
                        newCompany.setName(companyRequest.getName());
                        return companyRepository.save(newCompany);
                    });
        } else {
            throw new RuntimeException("Musisz podać ID firmy lub jej nazwę!");
        }

        driver.setCompany(finalCompany);
    }


}
