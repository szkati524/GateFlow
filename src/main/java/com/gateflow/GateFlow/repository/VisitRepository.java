package com.gateflow.GateFlow.repository;

import com.gateflow.GateFlow.Visit;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit,Long> {

    List<Visit> findByExitTimeIsNull();
}
