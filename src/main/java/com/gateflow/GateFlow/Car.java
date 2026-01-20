package com.gateflow.GateFlow;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String registrationNumber;
    private String brand;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;



    public Car(Long id, String registrationNumber, String brand, Company company) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.company = company;

    }

    public Car(){

    }

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



    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
