package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CarService {
    private final CarRepository carRepository;
    private final CompanyRepository companyRepository;

    public CarService(CarRepository carRepository, CompanyRepository companyRepository) {
        this.carRepository = carRepository;
        this.companyRepository = companyRepository;
    }

    private CarDto mapToDto(Car car) {
        String companyName = (car.getCompany() != null) ? car.getCompany().getName() : "brak";
        return new CarDto(
                car.getId(),
                car.getRegistrationNumber(),
                car.getBrand(),
                companyName,
                car.isActive()
        );
    }

    public List<CarDto> findAll() {
        return carRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    public CarDto findById(Long id) {
        return carRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono auta o ID: " + id));
    }

    public CarDto findByRegistrationNumber(String registrationNumber) {
        return carRepository.findByRegistrationNumberIgnoreCase(registrationNumber)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono auta o podanej rejestracji: " + registrationNumber));
    }

    public List<CarDto> findByBrand(String brand) {
        return carRepository.findByBrandIgnoreCase(brand).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<CarDto> findByCompanyName(String name) {
        return carRepository.findByCompanyNameIgnoreCase(name).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public CarDto addCar(CarDto dto) {
        Car car = new Car();
        car.setRegistrationNumber(dto.registrationNumber());
        car.setBrand(dto.brand());
        car.setActive(dto.active());

        if (dto.companyName() != null && !dto.companyName().isBlank()) {
            Company company = getOrCreateCompany(dto.companyName());
            car.setCompany(company);
        }

        Car saved = carRepository.save(car);
        return mapToDto(saved);
    }

    @Transactional
    public CarDto updateCar(Long id, CarDto requestDto) {
        Car carExisting = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono auta o ID: " + id));

        if (requestDto.registrationNumber() != null) {
            carExisting.setRegistrationNumber(requestDto.registrationNumber());
        }
        if (requestDto.brand() != null) {
            carExisting.setBrand(requestDto.brand());
        }
        carExisting.setActive(requestDto.active());

        if (requestDto.companyName() != null && !requestDto.companyName().isBlank()) {
            Company company = getOrCreateCompany(requestDto.companyName());
            carExisting.setCompany(company);
        }

        Car saved = carRepository.save(carExisting);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new RuntimeException("Nie można usunąć. Nie znaleziono auta o ID: " + id);
        }
        carRepository.deleteById(id);
    }

    private Company getOrCreateCompany(String companyName) {
        return companyRepository.findByNameIgnoreCase(companyName)
                .orElseGet(() -> {
                    Company newCompany = new Company();
                    newCompany.setName(companyName);
                    return companyRepository.save(newCompany);
                });
    }
}
