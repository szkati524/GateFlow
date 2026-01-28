package com.gateflow.GateFlow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Entity
@SoftDelete(columnName = "active",strategy = SoftDeleteType.ACTIVE)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String registrationNumber;
    private String brand;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
@Column(name = "active", insertable = false,updatable = false)
    private boolean active = true;


    public Car(Long id, String registrationNumber, String brand, Company company, boolean active) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.company = company;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
