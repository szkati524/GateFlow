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


    private final DriverModelAssembler assembler;
    private final DriverService driverService;

    public DriverController( DriverModelAssembler assembler, DriverService driverService) {
        this.assembler = assembler;
        this.driverService = driverService;
    }


    @GetMapping("/{id}")
    public DriverDto showDriver(@PathVariable Long id){
       return driverService.findById(id)
               .map(assembler::toModel)
               .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o Id " + id)    );


    }
    @PostMapping
    public ResponseEntity<DriverDto> addDriver(@RequestBody DriverDto driverDto){
        Driver createdDriver = driverService.addDriver(driverDto    );
        DriverDto model = assembler.toModel(createdDriver);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }
    @GetMapping
    public CollectionModel<DriverDto> getAllDrivers(){
        return assembler.toCollectionModel(driverService.findAllDrivers());

    }
    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id,@RequestBody DriverDto requestDto){
       Driver updatedDriver = driverService.updateDriver(id,requestDto);
     return assembler.toModel(updatedDriver);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id) {
        driverService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
