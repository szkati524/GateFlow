package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.dto.DriverDto;
import com.gateflow.GateFlow.model.Visit;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Long>, JpaSpecificationExecutor<Visit> {

    List<Visit> findByExitTimeIsNull();
    List<Visit> findByCompanyId(Long companyId);
    Optional<Visit> findFirstByCarRegistrationNumberAndExitTimeIsNullOrderByEntryTimeDesc(String registrationNumber);

    List<Visit> findByCompanyNameIgnoreCase(String companyName);

    @Query("""
    SELECT DISTINCT new com.gateflow.GateFlow.dto.DriverDto(
        v.driver.id,
        v.driver.name,
        v.driver.surname,
        v.company.name
    ) 
    FROM Visit v 
    WHERE LOWER(v.company.name) = LOWER(:companyName)
""")
    List<DriverDto> findUniqueDriversByCompanyName(@Param("companyName") String companyName);
}


