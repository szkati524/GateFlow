package com.gateflow.GateFlow.repository;


import com.gateflow.GateFlow.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    public Company findByNameIgnoreCase(String name);
}
