package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.assembler.VisitModelAssembler;
import com.gateflow.GateFlow.dto.EntityRequestDTO;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.*;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.DriverRepository;
import com.gateflow.GateFlow.repository.VisitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitModelAssembler assembler;
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
    private final DriverRepository driverRepository;
@Autowired
    public VisitService(VisitRepository visitRepository, VisitModelAssembler assembler, CarRepository carRepository, CompanyRepository companyRepository, DriverRepository driverRepository) {
        this.visitRepository = visitRepository;
    this.assembler = assembler;
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

        Company finalCompany = companyRepository.findByNameIgnoreCase(request.getCompanyName())
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setName(request.getCompanyName());
                    return companyRepository.save(newCompany);
                });


        Car finalCar = carRepository.findByRegistrationNumberIgnoreCase(request.getRegistrationNumber())
                .map(existingCar -> {
                    existingCar.setBrand(request.getBrand());
                    existingCar.setCompany(finalCompany);
                    return carRepository.save(existingCar);
                })
                .orElseGet(() -> {
                    Car newCar = new Car();
                    newCar.setRegistrationNumber(request.getRegistrationNumber());
                    newCar.setBrand(request.getBrand());
                    newCar.setCompany(finalCompany);
                    newCar.setActive(true);
                    return carRepository.save(newCar);
                });


        Driver finalDriver = driverRepository.findByNameAndSurnameIgnoreCase(
                request.getDriverName(),
                        request.getDriverSurname())
                .map(existingDriver -> {
                    existingDriver.setCompany(finalCompany);
                    return driverRepository.save(existingDriver);
                })
                .orElseGet(() -> {
                    Driver newDriver = new Driver();
                    newDriver.setName(request.getDriverName());
                    newDriver.setSurname(request.getDriverSurname());
                    newDriver.setCompany(finalCompany);
                    return driverRepository.save(newDriver);
                });


        Visit visit = Visit.builder()
                .car(finalCar)
                .company(finalCompany)
                .driver(finalDriver)
                .entryCargo(request.getCargo())
                .entryTime(LocalDateTime.now())
                .build();

        return visitRepository.save(visit);
    }
    public List<VisitDto> searchVisits(String reg, String name, String surname, String company, String brand, LocalTime entryTime, LocalTime exitTime, LocalDate entryDate) {





        String r = (reg != null && reg.trim().isEmpty()) ? null : reg;
        String n = (name != null && name.trim().isEmpty()) ? null : name;
        String s = (surname != null && surname.trim().isEmpty()) ? null : surname;
        String c = (company != null && company.isEmpty()) ? null : company;
        String b = (brand != null && brand.trim().isEmpty()) ? null : brand;


        Specification<Visit> spec = VisitSpecification.search(r, b, n, s, c, entryDate, entryTime, exitTime);

        List<Visit> results = visitRepository.findAll(spec);




        return results.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
    @Transactional
    public Visit registerExit(String registrationNumber,String exitCargo){
    Visit visit = visitRepository.findFirstByCarRegistrationNumberAndExitTimeIsNullOrderByEntryTimeDesc(registrationNumber)
            .orElseThrow(() -> new RuntimeException("Nie znaleziono aktywnej wizyty dla pojazdu " + registrationNumber) );

    visit.setExitTime(LocalDateTime.now());
    visit.setExitCargo(exitCargo);
    visit.getCar().setActive(false);
    return visitRepository.save(visit);
    }
    @Transactional
    public Visit registerExitById(Long id,String exitCargo) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wjazdu o ID " + id));
        checkIfAlreadyExited(visit);
        return completedVisit(visit, exitCargo);

    }
    @Transactional
    public Visit registerExitByRegistration(String registrationNumber,String exitCargo){
    Visit visit = visitRepository.findFirstByCarRegistrationNumberAndExitTimeIsNullOrderByEntryTimeDesc(registrationNumber)
            .orElseThrow(() -> new RuntimeException("Brak aktywnej wizyty na terenie dla pojazdu: " + registrationNumber));
  checkIfAlreadyExited(visit);
    return completedVisit(visit,exitCargo);
    }

    private void checkIfAlreadyExited(Visit visit){
    if (visit.getExitTime() != null){
        throw new RuntimeException("ta wizyta została zakończona pomyślnie");
    }
    }

    private Visit completedVisit(Visit visit,String cargo){
    visit.setExitCargo(cargo);
    visit.setExitTime(LocalDateTime.now());
    return visitRepository.save(visit);
    }


    }



