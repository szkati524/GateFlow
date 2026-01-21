package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.Visit;
import com.gateflow.GateFlow.repository.VisitRepository;
import com.gateflow.GateFlow.service.VisitService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/visits")
public class VisitController {
    private final VisitService visitService;
    private final VisitRepository visitRepository;

    public VisitController(VisitService visitService, VisitRepository visitRepository) {
        this.visitService = visitService;
        this.visitRepository = visitRepository;
    }

    @GetMapping("/test")
    public String test(){
        return "GateFlow gotowy na pierwszy wjazd";
    }
    @PostMapping("/entry")
    public EntityModel<Visit> registryEntry(@RequestBody Visit inputData){
     Visit newVisit = Visit.builder()
             .driver(inputData.getDriver())
             .car(inputData.getCar())
             .company(inputData.getCompany())
             .entryCargo(inputData.getEntryCargo())
             .entryTime(LocalDateTime.now())
             .build();
     Visit savedVisit = visitService.addVisit(newVisit);
     return EntityModel.of(savedVisit,linkTo(methodOn(VisitController.class).showVisit(savedVisit.getId())).withSelfRel(),
     linkTo(methodOn(VisitController.class)).withRel("all-visits"));

    }
    @GetMapping("/{id}")
    public EntityModel<Visit> showVisit(@PathVariable Long id){
        Visit visit = visitRepository.findById(id).orElseThrow();
        EntityModel<Visit> model = EntityModel.of(visit);
        model.add(linkTo(methodOn(VisitController.class).showVisit(id)).withSelfRel());
        model.add(linkTo(methodOn(VisitController.class).getAllVisits()).withRel("all-visits"));
        return model;

    }
    @GetMapping
    public CollectionModel<EntityModel<Visit>> getAllVisits(){
        List<EntityModel<Visit>> visits = visitRepository.findAll().stream()
                .map(visit -> EntityModel.of(visit,
                        linkTo(methodOn(VisitController.class).showVisit(visit.getId())).withSelfRel())).toList();
        return CollectionModel.of(visits,linkTo(methodOn(VisitController.class).getAllVisits()).withSelfRel());
    }
    @PutMapping("/{id}/exit")
    public EntityModel<Visit> exitVisit(@PathVariable Long id, @RequestBody String exitCargo){
        return visitRepository.findById(id)
                .map(v -> {
                    v.setExitCargo(exitCargo);
                    v.setExitTime(LocalDateTime.now());
                    Visit updatedVisit = visitService.addVisit(v);
                    return EntityModel.of(updatedVisit,linkTo(methodOn(VisitController.class).showVisit(id)).withSelfRel(),
                    linkTo(methodOn(VisitController.class).getAllVisits()).withRel("all_visits"));


                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono takiego wjazdu"));




        }
        @GetMapping("on-site")
    public CollectionModel<EntityModel<Visit>> getVisitsOnSite(){
          List<EntityModel<Visit>> currentVisits = visitRepository.findByExitTimeIsNull().stream()
                  .filter(visit -> visit.getExitTime() == null)
                  .map(visit -> EntityModel.of(visit,linkTo(methodOn(VisitController.class).showVisit(visit.getId())).withSelfRel())).toList();
return CollectionModel.of(currentVisits,linkTo(methodOn(VisitController.class).getVisitsOnSite()).withSelfRel());




        }
    }

