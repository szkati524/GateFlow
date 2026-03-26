package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.VisitModelAssembler;
import com.gateflow.GateFlow.dto.EntityRequestDTO;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Visit;
import com.gateflow.GateFlow.repository.VisitRepository;
import com.gateflow.GateFlow.service.VisitService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService visitService;
    private final VisitRepository visitRepository;
    private final VisitModelAssembler assembler;

    public VisitController(VisitService visitService, VisitRepository visitRepository, VisitModelAssembler assembler) {
        this.visitService = visitService;
        this.visitRepository = visitRepository;
        this.assembler = assembler;
    }

    @GetMapping("/test")
    public String test() {
        return "GateFlow gotowy na pierwszy wjazd";
    }

    @PostMapping("/entry")
    public ResponseEntity<VisitDto> registryEntry(@RequestBody EntityRequestDTO request) {
        Visit savedVisit = visitService.registryEntry(request);
        VisitDto dto = assembler.toModel(savedVisit);
        return ResponseEntity.created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);

    }

    @GetMapping("/{id}")
    public VisitDto showVisit(@PathVariable Long id) {
        return visitRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wizyty o ID: " + id));
    }

    @GetMapping
    public CollectionModel<VisitDto> getAllVisits() {
        return assembler.toCollectionModel(visitRepository.findAll());
    }

    @PutMapping("/{id}/exit")
        public VisitDto exitVisit(@PathVariable Long id, @RequestParam(required = false) String exitCargo) {
        Visit updatedVisit = visitService.registerExitById(id,exitCargo);
        return assembler.toModel(updatedVisit);


    }

    @GetMapping("/on-site")
    public CollectionModel<VisitDto> getVisitsOnSite() {
        List<Visit> activeVisits = visitRepository.findByExitTimeIsNull();
        return assembler.toCollectionModel(activeVisits);


    }

    @PutMapping("/exit/{registrationNumber}")
    public VisitDto registerExit(@PathVariable String registrationNumber, @RequestParam(required = false) String exitCargo) {
        Visit updatedVisit = visitService.registerExitByRegistration(registrationNumber, exitCargo);
        return assembler.toModel(updatedVisit);
    }
}

