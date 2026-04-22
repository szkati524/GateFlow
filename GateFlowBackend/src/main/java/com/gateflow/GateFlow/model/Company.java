package com.gateflow.GateFlow.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity

public class Company {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String name;
    @OneToMany(mappedBy = "company")
    private List<Driver> driverList = new ArrayList<>();
    @OneToMany(mappedBy = "company")
    private List<Car> cars = new ArrayList<>();

    public Company(String name, List<Driver> driverList, List<Car> cars) {
        this.name = name;
        this.driverList = driverList;
        this.cars = cars;
    }
    public Company(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(driverList, company.driverList) && Objects.equals(cars, company.cars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, driverList, cars);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", driverList=" + driverList +
                ", cars=" + cars +
                '}';
    }
}

