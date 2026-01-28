package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
@Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void addCar(Car car){
        carRepository.save(car);
    }
    public List<Car> findAll(){
        return carRepository.findAll();
    }
    public Optional<Car> findById(Long id){
        return carRepository.findById(id);
    }
    public void deleteById(Long id){
        carRepository.deleteById(id);
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
}
