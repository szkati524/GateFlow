package com.gateflow.GateFlow.dto;

import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import org.springframework.hateoas.RepresentationModel;

public record CarDto (
        Long id,
        String registrationNumber,
        String brand,
        String companyName,
        boolean active
) {

}
