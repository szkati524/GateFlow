package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
    public Optional<Driver> findByNameAndSurnameIgnoreCase(String name, String surname);
    public List<Driver> findByCompanyNameIgnoreCase(String name);
    public List<Driver> findByCompanyId(Long id);
}
