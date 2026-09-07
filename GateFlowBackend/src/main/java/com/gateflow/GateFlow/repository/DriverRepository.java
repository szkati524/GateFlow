package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
    Optional<Driver> findFirstByNameAndSurnameIgnoreCase(String name, String surname);


    List<Driver> findByCompanyNameIgnoreCase(String companyName);


    List<Driver> findByCompanyId(Long companyId);
}


