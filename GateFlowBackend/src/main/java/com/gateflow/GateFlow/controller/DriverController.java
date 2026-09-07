package com.gateflow.GateFlow.controller;


import com.gateflow.GateFlow.dto.DriverDto;
import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.CompanyRepository;
import com.gateflow.GateFlow.repository.DriverRepository;
import com.gateflow.GateFlow.service.DriverService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/drivers")
public class DriverController {



    private final DriverService driverService;

    public DriverController(  DriverService driverService) {
        this.driverService = driverService;
    }


    @GetMapping("/{id}")
    public DriverDto showDriver(@PathVariable Long id){
       return driverService.findById(id);


    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDto addDriver(@RequestBody DriverDto driverDto){
        return driverService.addDriver(driverDto);
    }
    @GetMapping("/company/{companyId}")
    public List<DriverDto> getDriversByCompany(@PathVariable Long companyId) {
        return driverService.findByCompanyId(companyId);
    }
    @GetMapping
    public List<DriverDto> getAllDrivers(){
        return driverService.findAllDrivers();

    }
    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id, @RequestBody DriverDto requestDto) {
        return driverService.updateDriver(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
