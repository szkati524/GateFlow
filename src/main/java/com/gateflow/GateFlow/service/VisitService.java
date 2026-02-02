package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.EntityRequestDTO;
import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.model.Visit;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.DriverRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final DriverRepository driverRepository;
@Autowired
    public VisitService(VisitRepository visitRepository, CarRepository carRepository, CompanyRepository companyRepository, DriverRepository driverRepository) {
        this.visitRepository = visitRepository;
    this.carRepository = carRepository;
    this.companyRepository = companyRepository;
    this.driverRepository = driverRepository;
}

    public Visit addVisit(Visit visit){
    return visitRepository.save(visit);
    }
    public Optional<Visit> findVisitById(Long id){
    return visitRepository.findById(id);
    }
    public List<Visit> findAllVisits(){
    return visitRepository.findAll();
    }
    public void deleteVisitById(Long id){
    visitRepository.deleteById(id);
    }
    public List<Visit> findByExitTimeIsNull(){
    return visitRepository.findByExitTimeIsNull();
    }
    @Transactional
    public Visit registryEntry(EntityRequestDTO request){

        Company finalCompany = companyRepository.findByNameIgnoreCase(request.getCompany().getName())
                .orElseGet(() -> companyRepository.save(request.getCompany()));


        Car finalCar = carRepository.findByRegistrationNumberIgnoreCase(request.getCar().getRegistrationNumber())
                .map(existingCar -> {
                    existingCar.setBrand(request.getCar().getBrand());
                    existingCar.setCompany(finalCompany);
                    return carRepository.save(existingCar);
                })
                .orElseGet(() -> {
                    request.getCar().setCompany(finalCompany);
                    return carRepository.save(request.getCar());
                });


        Driver finalDriver = driverRepository.findByNameAndSurnameIgnoreCase(request.getDriver().getName(), request.getDriver().getSurname())
                .orElseGet(() -> driverRepository.save(request.getDriver()));


        Visit visit = Visit.builder()
                .car(finalCar)
                .company(finalCompany)
                .driver(finalDriver)
                .entryCargo(request.getCargo())
                .entryTime(LocalDateTime.now())
                .build();

        return visitRepository.save(visit);
    }
    public Visit registerExit(String registrationNumber,String exitCargo){
    Visit visit = visitRepository.findFirstByCarRegistrationNumberAndExitTimeIsNullOrderByEntryTimeDesc(registrationNumber)
            .orElseThrow(() -> new RuntimeException("Nie znaleziono aktywnej wizyty dla pojazdu " + registrationNumber) );

    visit.setExitTime(LocalDateTime.now());
    visit.setExitCargo(exitCargo);
    return visitRepository.save(visit);
    }


    }



