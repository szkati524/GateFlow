package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.model.Visit;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Long> {

    List<Visit> findByExitTimeIsNull();
    List<Visit> findByCompanyId(Long companyId);
    Optional<Visit> findFirstByCarRegistrationNumberAndExitTimeIsNullOrderByEntryTimeDesc(String registrationNumber);
}
