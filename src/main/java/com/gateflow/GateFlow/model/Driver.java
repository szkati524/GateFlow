package com.gateflow.GateFlow.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gateflow.GateFlow.model.Company;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public Driver(Long id,String name, String surname, Company company) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.company = company;

    }
    public Driver(){

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Company getCompany() {
        return company;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id) && Objects.equals(name, driver.name) && Objects.equals(surname, driver.surname) && Objects.equals(company, driver.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, company);
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", company=" + company +
                '}';
    }
}
