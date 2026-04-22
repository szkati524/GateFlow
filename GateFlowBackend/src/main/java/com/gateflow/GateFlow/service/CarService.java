package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;
@Autowired
    public CarService(CarRepository carRepository, CompanyRepository companyRepository) {
        this.carRepository = carRepository;
    this.companyRepository = companyRepository;
}

    public Car addCar(CarDto dto){
       Car car = new Car();
       car.setRegistrationNumber(dto.getRegistrationNumber());
       car.setBrand(dto.getBrand());
       if (dto.getCompanyName() != null ){
           Company company = companyRepository.findByNameIgnoreCase(dto.getCompanyName())
                   .orElseGet(() -> {
                       Company newCompany = new Company();
                              newCompany.setName(dto.getCompanyName());
                              return companyRepository.save(newCompany);
                   });
           car.setCompany(company);
       }
       return carRepository.save(car);
    }
    public List<Car> findAll(){
        return carRepository.findAll();
    }
    public Optional<Car> findById(Long id){
        return carRepository.findById(id);
    }
    @Transactional
    public void deleteCar(Long id){
      Car car = carRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("Nie można usunąć. Nie znalezono auta o ID: " + id));
      carRepository.delete(car);
    }
    public List<Car> findByBrand(String brand){
    return carRepository.findByBrandIgnoreCase(brand);
    }
    public Optional<Car> findByRegistrationNumber(String registrationNumber){
    return carRepository.findByRegistrationNumberIgnoreCase(registrationNumber);
    }
    public List<Car> findByCompanyName(String name){
    return carRepository.findByCompanyNameIgnoreCase(name);
    }
    @Transactional
    public Car updatedCar(Long id,CarDto requestDto){
    return carRepository.findById(id)
            .map(carExisting -> {
                if (requestDto.getRegistrationNumber() != null){
                    carExisting.setRegistrationNumber(requestDto.getRegistrationNumber());
                }
                if (requestDto.getBrand() != null){
                    carExisting.setBrand(requestDto.getBrand());
                }
                if (requestDto.getCompanyName() != null && !requestDto.getCompanyName().isBlank()){
                    Company company = companyRepository.findByNameIgnoreCase(requestDto.getCompanyName())
                            .orElseGet(() -> {
                                Company newCompany = new Company();
                                newCompany.setName(requestDto.getCompanyName());
                                return companyRepository.save(newCompany);
                            });
                    carExisting.setCompany(company);
                }
                return carRepository.save(carExisting);
            })
            .orElseThrow(() -> new RuntimeException("Nie znaleziono auta o ID: " + id)  );
    }
}
