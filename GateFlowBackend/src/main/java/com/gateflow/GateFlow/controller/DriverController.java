package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.assembler.DriverModelAssembler;
import com.gateflow.GateFlow.dto.DriverDto;
import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.DriverRepository;
import com.gateflow.GateFlow.service.DriverService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverRepository driverRepository;
    private final CompanyRepository companyRepository;
    private final DriverModelAssembler assembler;
    private final DriverService driverService;

    public DriverController(DriverRepository driverRepository, CompanyRepository companyRepository, DriverModelAssembler assembler, DriverService driverService) {
        this.driverRepository = driverRepository;
        this.companyRepository = companyRepository;
        this.assembler = assembler;
        this.driverService = driverService;
    }


    @GetMapping("/{id}")
    public DriverDto showDriver(@PathVariable Long id){
        return driverRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new RuntimeException("nie znaleziono kierowcy o ID: " + id)  );

    }
    @PostMapping
    public ResponseEntity<DriverDto> addDriver(@RequestBody Driver driver){
        Driver createdDriver = driverRepository.save(driver);
        DriverDto dto = assembler.toModel(createdDriver);
        return ResponseEntity.created(dto.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(dto);
    }
    @GetMapping
    public CollectionModel<DriverDto> getAllDrivers(){
        return assembler.toCollectionModel(driverRepository.findAll());

    }
    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id,@RequestBody Driver requestDriver){
       Driver updatedDriver = driverService.updateDriver(id,requestDriver);
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
