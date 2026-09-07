package com.gateflow.GateFlow.assembler;

import com.gateflow.GateFlow.controller.VisitController;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Visit;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VisitModelAssembler {


    public VisitDto toModel(Visit visit) {
        if (visit == null) return null;

        LocalDateTime endTime = (visit.getExitTime() != null) ? visit.getExitTime() : LocalDateTime.now();
        long duration = (visit.getEntryTime() != null)
                ? Duration.between(visit.getEntryTime(), endTime).toMinutes()
                : 0L;

        String regNum = (visit.getCar() != null) ? visit.getCar().getRegistrationNumber() : null;
        String driverName = (visit.getDriver() != null) ? visit.getDriver().getName() : null;
        String surname = (visit.getDriver() != null) ? visit.getDriver().getSurname() : null;
        String compName = (visit.getCompany() != null) ? visit.getCompany().getName() : null;
        LocalDate entryDate = (visit.getEntryTime() != null) ? visit.getEntryTime().toLocalDate() : null;
        String status = (visit.getExitTime() == null) ? "ACTIVE" : "COMPLETED";

        return new VisitDto(
                visit.getId(),
                regNum,
                driverName,
                surname,
                compName,
                visit.getEntryTime(),
                visit.getExitTime(),
                entryDate,
                visit.getEntryCargo(),
                visit.getExitCargo(),
                duration,
                status
        );
    }
}


