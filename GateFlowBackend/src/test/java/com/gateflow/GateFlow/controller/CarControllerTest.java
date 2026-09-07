package com.gateflow.GateFlow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateflow.GateFlow.config.JwtUtils;
import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CarController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser
    void getAllCars_ShouldReturnCarListAnd200() throws Exception {
        CarDto car1 = new CarDto(1L, "SK12345", "BMW", "Firma A", true);
        CarDto car2 = new CarDto(2L, "KR99999", "Audi", "brak", false);
        when(carService.findAll()).thenReturn(List.of(car1, car2));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].registrationNumber").value("SK12345"))
                .andExpect(jsonPath("$[1].registrationNumber").value("KR99999"));
    }

    @Test
    @WithMockUser
    void showCar_ShouldReturnCar_WhenExists() throws Exception {
        CarDto car = new CarDto(1L, "SK12345", "BMW", "Firma A", true);
        when(carService.findById(1L)).thenReturn(car);

        mockMvc.perform(get("/api/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("BMW"));
    }

    @Test
    @WithMockUser
    void findByRegistration_ShouldReturnCar() throws Exception {
        CarDto car = new CarDto(1L, "SK12345", "BMW", "Firma A", true);
        when(carService.findByRegistrationNumber("SK12345")).thenReturn(car);

        mockMvc.perform(get("/api/cars/registration/SK12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("SK12345"));
    }

    @Test
    @WithMockUser
    void addCar_ShouldCreateCarAndReturn201() throws Exception {
        CarDto inputDto = new CarDto(null, "SK12345", "BMW", "Firma A", true);
        CarDto createdDto = new CarDto(1L, "SK12345", "BMW", "Firma A", true);
        when(carService.addCar(any(CarDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.registrationNumber").value("SK12345"));
    }

    @Test
    @WithMockUser
    void updateCar_ShouldUpdateCarAndReturn200() throws Exception {
        CarDto requestDto = new CarDto(null, "SK12345-NEW", "BMW", "Firma A", true);
        CarDto updatedDto = new CarDto(1L, "SK12345-NEW", "BMW", "Firma A", true);
        when(carService.updateCar(eq(1L), any(CarDto.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("SK12345-NEW"));
    }

    @Test
    @WithMockUser
    void deleteCar_ShouldReturn204NoContent() throws Exception {
        doNothing().when(carService).deleteCar(1L);

        mockMvc.perform(delete("/api/cars/1"))
                .andExpect(status().isNoContent());
    }
}