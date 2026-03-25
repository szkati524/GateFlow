package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.DriverModelAssembler;
import com.gateflow.GateFlow.dto.DriverDto;
import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.DriverRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("drivers")
public class DriverController {

    private final DriverRepository driverRepository;
    private final DriverModelAssembler assembler;

    public DriverController(DriverRepository driverRepository, DriverModelAssembler assembler) {
        this.driverRepository = driverRepository;
        this.assembler = assembler;
    }


    @GetMapping("/{id}")
    public DriverDto showDriver(@PathVariable Long id){
        return driverRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new RuntimeException("nie znaleziono kierowcy o ID: " + id)  );

    }
    @GetMapping
    public CollectionModel<DriverDto> getAllDrivers(){
        return assembler.toCollectionModel(driverRepository.findAll());

    }
    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id,@RequestBody Driver requestDriver){
       Driver updatedDriver = driverRepository.findById(id)
                .map(driverExisting -> {
                    if (requestDriver.getName() != null){
                        driverExisting.setName(requestDriver.getName());
                    }
                    if (requestDriver.getSurname() != null){
                        driverExisting.setSurname(requestDriver.getSurname());
                    }
                    if (requestDriver.getCompany() != null){
                        driverExisting.setCompany( requestDriver.getCompany());
                    }
                  return driverRepository.save(driverExisting);
                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o danym ID " + id) );
       return assembler.toModel(updatedDriver);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id){
        return driverRepository.findById(id)
                .map(driver -> {
                    driverRepository.delete(driver);
                   return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
