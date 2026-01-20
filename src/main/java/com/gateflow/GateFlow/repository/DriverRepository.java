package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.Company;
import com.gateflow.GateFlow.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {
    public Driver findByNameAndSurnameIgnoreCase(String name,String surname);
    public List<Driver> findByCompanyIgnoreCase(Company company);
    public List<Driver> findByCompanyId(Long id);
}
