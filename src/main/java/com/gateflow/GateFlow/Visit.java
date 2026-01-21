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

    public Visit() {

    }


    private Visit(VisitBuilder builder) {
        this.id = builder.id;
        this.driver = builder.driver;
        this.car = builder.car;
        this.company = builder.company;
        this.entryCargo = builder.entryCargo;
        this.exitCargo = builder.exitCargo;
        this.entryTime = builder.entryTime;
        this.exitTime = builder.exitTime;
    }

    public static class VisitBuilder {
        private Long id;
        private Driver driver;
        private Car car;
        private Company company;
        private LocalDateTime entryTime;
        private LocalDateTime exitTime;
        private String entryCargo;
        private String exitCargo;

        public VisitBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public VisitBuilder driver(Driver driver) {
            this.driver = driver;
            return this;
        }

        public VisitBuilder car(Car car) {
            this.car = car;
            return this;
        }

        public VisitBuilder company(Company company) {
            this.company = company;
            return this;
        }

        public VisitBuilder entryTime(LocalDateTime entryTime) {
            this.entryTime = entryTime;
            return this;
        }

        public VisitBuilder entryCargo(String entryCargo) {
            this.entryCargo = entryCargo;
            return this;
        }
        public VisitBuilder exitTime(LocalDateTime exitTime){
            this.exitTime = exitTime;
            return this;
        }
        public VisitBuilder exitCargo(String exitCargo){
            this.exitCargo = exitCargo;
            return this;
        }

        public Visit build() {
            return new Visit(this);
        }
    }

    public static VisitBuilder builder() {
        return new VisitBuilder();
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



