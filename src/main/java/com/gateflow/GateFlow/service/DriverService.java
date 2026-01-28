package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
@Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
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


}
