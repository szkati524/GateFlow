package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    public List<Car> findByBrandIgnoreCase(String brand);
    public Optional<Car> findByRegistrationNumberIgnoreCase(String registrationNumber);
    public List<Car> findByCompanyNameIgnoreCase(String name);
}
