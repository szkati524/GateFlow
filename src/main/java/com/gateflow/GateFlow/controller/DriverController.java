package com.gateflow.GateFlow.controller;

import com.gateflow.GateFlow.model.Driver;
import com.gateflow.GateFlow.repository.DriverRepository;
import com.gateflow.GateFlow.service.DriverService;
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
    private final DriverService driverService;

    public DriverController(DriverRepository driverRepository, DriverService driverService) {
        this.driverRepository = driverRepository;
        this.driverService = driverService;
    }

    @GetMapping("/{id}")
    public EntityModel<Driver> showDriver(@PathVariable Long id){
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o podanym ID " + id));

      return   EntityModel.of(driver,linkTo(methodOn(DriverController.class).showDriver(id)).withSelfRel(),
              linkTo(methodOn(DriverController.class).getAllDrivers()).withRel("all-drivers")   );
    }
    @GetMapping
    public CollectionModel<EntityModel<Driver>> getAllDrivers(){
        List<EntityModel<Driver>> drivers = driverRepository.findAll().stream()
                .map(d -> EntityModel.of(d,
                        linkTo(methodOn(DriverController.class).showDriver(d.getId())).withSelfRel()))
                        .collect(Collectors.toList());
                        return CollectionModel.of(drivers,linkTo(methodOn(DriverController.class).getAllDrivers()).withRel("all-drivers"));

    }
    @PutMapping("/{id}")
    public EntityModel<Driver> updateDriver(@PathVariable Long id,@RequestBody Driver requuestDriver){
        return driverRepository.findById(id)
                .map(driverExisting -> {
                    if (requuestDriver.getName() != null){
                        driverExisting.setName(driverExisting.getName());
                    }
                    if (requuestDriver.getSurname() != null){
                        driverExisting.setSurname(driverExisting.getSurname());
                    }
                    if (requuestDriver.getCompany() != null){
                        driverExisting.setCompany( driverExisting.getCompany());
                    }
                    Driver updatedDriver = driverRepository.save(driverExisting);
                    return EntityModel.of(updatedDriver,linkTo(methodOn(DriverController.class).showDriver(id)).withSelfRel(),
                            linkTo(methodOn(DriverController.class).getAllDrivers()).withRel("all-drivers"));
                })
                .orElseThrow(() -> new RuntimeException("Nie znaleziono kierowcy o danym ID " + id) );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id){
        return driverRepository.findById(id)
                .map(driver -> {
                    driverRepository.delete(driver);
                    var link = linkTo(methodOn(DriverController.class).getAllDrivers()).withRel("all-drivers");
                    return ResponseEntity.ok(CollectionModel.empty(link));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
