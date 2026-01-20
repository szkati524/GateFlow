package com.gateflow.GateFlow;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String entryCargo;
    private String exitCargo;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Visit(Long id, Driver driver, Car car, Company company, String entryCargo, String exitCargo, LocalDateTime entryTime, LocalDateTime exitTime) {
        this.id = id;
        this.driver = driver;
        this.car = car;
        this.company = company;
        this.entryCargo = entryCargo;
        this.exitCargo = exitCargo;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    public Visit(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getEntryCargo() {
        return entryCargo;
    }

    public void setEntryCargo(String entryCargo) {
        this.entryCargo = entryCargo;
    }

    public String getExitCargo() {
        return exitCargo;
    }

    public void setExitCargo(String exitCargo) {
        this.exitCargo = exitCargo;
    }
}

