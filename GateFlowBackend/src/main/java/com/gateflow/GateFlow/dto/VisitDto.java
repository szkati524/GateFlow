package com.gateflow.GateFlow.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Relation(collectionRelation = "visits",itemRelation = "visit")
public class VisitDto extends RepresentationModel<VisitDto> {
    private Long id;


    private String registrationNumber;
    private String driverFullName;
    private String companyName;


    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private LocalDate entryDate;
    private String entryCargo;
    private String exitCargo;


    private long durationMinutes;
    private String status;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getDriverFullName() { return driverFullName; }
    public void setDriverFullName(String driverFullName) { this.driverFullName = driverFullName; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public String getEntryCargo() { return entryCargo; }
    public void setEntryCargo(String entryCargo) { this.entryCargo = entryCargo; }
    public String getExitCargo() { return exitCargo; }
    public void setExitCargo(String exitCargo) { this.exitCargo = exitCargo; }
    public long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(long durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
}

