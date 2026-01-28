package com.gateflow.GateFlow.repository;


import com.gateflow.GateFlow.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    public Optional<Company> findByNameIgnoreCase(String name);
}
