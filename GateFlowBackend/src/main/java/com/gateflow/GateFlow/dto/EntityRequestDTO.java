package com.gateflow.GateFlow.dto;

import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.model.Driver;

import java.util.Objects;

public class EntityRequestDTO {
    private Car car;
    private Company company;
    private Driver driver;
    private String cargo;

    public EntityRequestDTO(){

    }

    public EntityRequestDTO(Car car, Company company, Driver driver,String cargo) {
        this.car = car;
        this.company = company;
        this.driver = driver;
        this.cargo = cargo;
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

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityRequestDTO that = (EntityRequestDTO) o;
        return Objects.equals(car, that.car) && Objects.equals(company, that.company) && Objects.equals(driver, that.driver) && Objects.equals(cargo, that.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, company, driver, cargo);
    }

    @Override
    public String toString() {
        return "EntityRequestDTO{" +
                "car=" + car +
                ", company=" + company +
                ", driver=" + driver +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
