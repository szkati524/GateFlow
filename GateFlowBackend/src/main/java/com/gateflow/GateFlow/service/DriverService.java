package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.DriverDto;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.DriverRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DriverService {

    private final DriverRepository driverRepository;
    private final CompanyRepository companyRepository;

    public DriverService(DriverRepository driverRepository, CompanyRepository companyRepository) {
        this.driverRepository = driverRepository;
    this.companyRepository = companyRepository;
}
private DriverDto mapToDto(Driver driver){
        String companyName = (driver.getCompany() != null ) ? driver.getCompany().getName() : "brak";
        return new DriverDto(
                driver.getId(),
                driver.getName(),
                driver.getSurname(),
                companyName
        );


}

    public DriverDto findById(Long id){
        return driverRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o ID: " + id));

    }
    public List<DriverDto> findAllDrivers(){
        return driverRepository.findAll()
                .stream().map(this::mapToDto)
                .toList();
    }
    public Optional<Driver> findByFullName(String name,String surname){
        return driverRepository.findFirstByNameAndSurnameIgnoreCase(name,surname);
    }


    public List<DriverDto> findByCompanyId(Long id){
        return driverRepository.findByCompanyId(id)
                .stream().map(this::mapToDto)
                .toList();
    }
    @Transactional
    public DriverDto addDriver(DriverDto dto){
        Driver driver = new Driver();
        driver.setName(dto.name());
        driver.setSurname(dto.surname());
        assignCompanyToDriver(driver,dto.companyName());
        Driver saved = driverRepository.save(driver);
        return mapToDto(saved);
    }
    @Transactional
    public DriverDto updateDriver(Long id,DriverDto requestDto){
        Driver existing = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o ID " + id));

        if (requestDto.name() != null) existing.setName(requestDto.name());
        if (requestDto.surname() != null) existing.setSurname(requestDto.surname());

        if (requestDto.companyName() != null && !requestDto.companyName().isBlank()) {
            assignCompanyToDriver(existing, requestDto.companyName());
        }

        Driver saved = driverRepository.save(existing);
        return mapToDto(saved);

    }
    @Transactional
    public void deleteById(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new RuntimeException("Nie znaleziono kierowcy o ID " + id);
        }
        driverRepository.deleteById(id);
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
