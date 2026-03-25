package com.gateflow.GateFlow.assembler;

import com.gateflow.GateFlow.controller.VisitController;
import com.gateflow.GateFlow.dto.VisitDto;
import com.gateflow.GateFlow.model.Visit;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VisitModelAssembler extends RepresentationModelAssemblerSupport<Visit,VisitDto> {

    public VisitModelAssembler() {
        super(VisitController.class, VisitDto.class);
    }

    @Override
    public VisitDto toModel(Visit visit) {
        VisitDto dto = createModelWithId(visit.getId(),visit);
        dto.setId(visit.getId());
        if(visit.getCar() != null){
            dto.setRegistrationNumber(visit.getCar().getRegistrationNumber());
        }
        if(visit.getDriver() != null){
            dto.setDriverFullName(visit.getDriver().getName() + " " + visit.getDriver().getSurname());
        }
        if(visit.getCompany() != null){
            dto.setCompanyName(visit.getCompany().getName());
        }
        dto.setEntryCargo(visit.getEntryCargo());
        dto.setExitCargo(visit.getExitCargo());
        dto.setEntryTime(visit.getEntryTime());
        dto.setExitTime(visit.getExitTime());
        LocalDateTime endTime = (visit.getExitTime() != null) ? visit.getExitTime() : LocalDateTime.now();
        if (visit.getEntryTime() != null){
            dto.setDurationMinutes(Duration.between(visit.getEntryTime(),endTime).toMinutes());
        }
        dto.setStatus(visit.getExitTime() == null ? "ACTIVE" : "COMPLETED");
        return dto;
    }
    @Override
    public CollectionModel<VisitDto> toCollectionModel(Iterable<? extends Visit> entities){
        CollectionModel<VisitDto> visitModels = super.toCollectionModel(entities);
        visitModels.add(linkTo(methodOn(VisitController.class).getAllVisits()).withSelfRel());
        return visitModels;
    }
}
