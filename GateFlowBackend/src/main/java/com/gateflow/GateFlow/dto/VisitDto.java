package com.gateflow.GateFlow.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Relation(collectionRelation = "visits", itemRelation = "visit")
public record VisitDto(
        Long id,
        String registrationNumber,
        String driverName,
        String surname,
        String companyName,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        LocalDate entryDate,
        String entryCargo,
        String exitCargo,
        long durationMinutes,
        String status
) {

}
