package com.gateflow.GateFlow.service;

import com.gateflow.GateFlow.Visit;
import com.gateflow.GateFlow.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
@Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit addVisit(Visit visit){
    return visitRepository.save(visit);
    }
    public Optional<Visit> findVisitById(Long id){
    return visitRepository.findById(id);
    }
    public List<Visit> findAllVisits(){
    return visitRepository.findAll();
    }
    public void deleteVisitById(Long id){
    visitRepository.deleteById(id);
    }
    public List<Visit> findByExitTimeIsNull(){
    return visitRepository.findByExitTimeIsNull();
    }

}

