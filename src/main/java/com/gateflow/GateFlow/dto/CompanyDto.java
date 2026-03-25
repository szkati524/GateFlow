package com.gateflow.GateFlow.dto;

import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Driver;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class CompanyDto extends RepresentationModel<CompanyDto> {
    private Long id;
    private String name;


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


}
