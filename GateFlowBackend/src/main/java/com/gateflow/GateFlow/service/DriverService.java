package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.DriverDto;
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

    public DriverService(DriverRepository driverRepository, CompanyRepository companyRepository) {
        this.driverRepository = driverRepository;
    this.companyRepository = companyRepository;
}
private DriverDto mapToDto(Driver driver){
        DriverDto dto = new DriverDto();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setSurname(driver.getSurname());
        if (driver.getCompany() != null){
            dto.setCompanyName(driver.getCompany().getName());
        } else {
            dto.setCompanyName("brak");
        }
        return dto;
}
@Transactional
    public Driver addDriver(DriverDto dto){
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setSurname(dto.getSurname());
        assignCompanyToDriver(driver,dto.getCompanyName());
        return driverRepository.save(driver);
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
    @Transactional
    public void deleteById(Long id){
        if (!driverRepository.existsById(id)){
            throw new RuntimeException("Nie znaleziono kierowcy o Id " + id);
        }
        driverRepository.deleteById(id);
    }

    public List<Driver> findByCompanyId(Long id){
        return driverRepository.findByCompanyId(id);
    }
    @Transactional
    public Driver updateDriver(Long id,DriverDto requestDto){
    Driver existing = driverRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o ID " + id)   );
    if (requestDto.getName() != null) existing.setName(requestDto.getName());
    if (requestDto.getSurname() != null) existing.setSurname(requestDto.getSurname());

    if (requestDto.getCompanyName() != null && !requestDto.getCompanyName().isBlank()) {
        assignCompanyToDriver(existing,requestDto.getCompanyName());
    }
    return driverRepository.save(existing);
    }


    private void assignCompanyToDriver(Driver driver,String companyName){
        if (companyName == null || companyName.isBlank()) return;
        Company company = companyRepository.findByNameIgnoreCase(companyName)
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setName(companyName);
                    return companyRepository.save(newCompany);
                });
        driver.setCompany(company);
    }


}
