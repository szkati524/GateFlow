package com.gateflow.GateFlow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.util.Objects;

@Entity
@SoftDelete(columnName = "active",strategy = SoftDeleteType.ACTIVE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return active == car.active && Objects.equals(id, car.id) && Objects.equals(registrationNumber, car.registrationNumber) && Objects.equals(brand, car.brand) && Objects.equals(company, car.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registrationNumber, brand, company, active);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", company=" + company +
                ", active=" + active +
                '}';
    }
}
