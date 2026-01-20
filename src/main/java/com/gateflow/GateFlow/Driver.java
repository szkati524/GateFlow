package com.gateflow.GateFlow;


import jakarta.persistence.*;

import java.util.List;
@Entity
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


}
