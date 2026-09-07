package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.VisitModelAssembler;
import com.gateflow.GateFlow.dto.EntityRequestDTO;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Visit;
import com.gateflow.GateFlow.repository.VisitRepository;
import com.gateflow.GateFlow.service.VisitService;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/visits")
public class VisitController {
    private final VisitService visitService;
    private final VisitModelAssembler assembler;

    public VisitController(VisitService visitService, VisitModelAssembler assembler) {
        this.visitService = visitService;
        this.assembler = assembler;
    }

    @PostMapping("/entry")
    public ResponseEntity<VisitDto> registryEntry(@RequestBody EntityRequestDTO request) {
        Visit savedVisit = visitService.registryEntry(request);
        VisitDto dto = assembler.toModel(savedVisit);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/{id}")
    public VisitDto showVisit(@PathVariable Long id) {
        return visitService.findVisitById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wizyty o ID: " + id));
    }

    @GetMapping
    public List<VisitDto> getAllVisits() {
        return visitService.findAllVisits().stream()
                .map(assembler::toModel)
                .toList();
    }

    @PutMapping("/{id}/exit")
    public VisitDto exitVisit(@PathVariable Long id, @RequestParam(required = false) String exitCargo) {
        Visit updatedVisit = visitService.registerExitById(id, exitCargo);
        return assembler.toModel(updatedVisit);
    }

    @GetMapping("/on-site")
    public List<VisitDto> getVisitsOnSite() {
        return visitService.findByExitTimeIsNull().stream()
                .map(assembler::toModel)
                .toList();
    }

    @PutMapping("/exit/{registrationNumber}")
    public VisitDto registerExit(@PathVariable String registrationNumber, @RequestParam(required = false) String exitCargo) {
        Visit updatedVisit = visitService.registerExitByRegistration(registrationNumber, exitCargo);
        return assembler.toModel(updatedVisit);
    }

    @GetMapping("/search")
    public List<VisitDto> searchVisits(
            @RequestParam(required = false) String reg,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime entryTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime exitTime
    ) {
        return visitService.searchVisits(reg, name, surname, company, brand, entryDate, entryTime, exitTime);
    }
}

