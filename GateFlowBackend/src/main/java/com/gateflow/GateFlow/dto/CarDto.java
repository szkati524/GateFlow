package com.gateflow.GateFlow.dto;

import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import org.springframework.hateoas.RepresentationModel;

public class CarDto extends RepresentationModel<CarDto> {
    private Long id;
    private String registrationNumber;
    private String brand;
    private String companyName;
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
