package com.gateflow.GateFlow.dto;

import org.springframework.hateoas.RepresentationModel;

public record DriverDto (
        Long id,
        String name,
        String surname,
        String companyName
){


}
