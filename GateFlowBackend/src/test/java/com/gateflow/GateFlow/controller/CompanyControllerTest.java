package com.gateflow.GateFlow.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateflow.GateFlow.config.JwtUtils;
import com.gateflow.GateFlow.dto.CompanyCreateDto;
import com.gateflow.GateFlow.dto.CompanyDto;
import com.gateflow.GateFlow.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(controllers = CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser
    void getAllCompanies_ShouldReturnCompanyListAnd200() throws Exception {
        CompanyDto company1 = new CompanyDto(1L, "Firma A");
        CompanyDto company2 = new CompanyDto(2L, "Firma B");
        when(companyService.findAll()).thenReturn(List.of(company1, company2));

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Firma A"))
                .andExpect(jsonPath("$[1].name").value("Firma B"));
    }

    @Test
    @WithMockUser
    void getCompanyById_ShouldReturnCompany_WhenExists() throws Exception {
        CompanyDto company = new CompanyDto(1L, "Firma A");
        when(companyService.findById(1L)).thenReturn(company);

        mockMvc.perform(get("/api/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Firma A"));
    }

    @Test
    @WithMockUser
    void addCompany_ShouldCreateCompanyAndReturn201() throws Exception {
        CompanyCreateDto createDto = new CompanyCreateDto("Firma A");
        CompanyDto createdDto = new CompanyDto(1L, "Firma A");
        when(companyService.addCompany(any(CompanyCreateDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Firma A"));
    }

    @Test
    @WithMockUser
    void updateCompany_ShouldUpdateCompanyAndReturn200() throws Exception {
        CompanyCreateDto updateDto = new CompanyCreateDto("Nowa Nazwa");
        CompanyDto updatedDto = new CompanyDto(1L, "Nowa Nazwa");
        when(companyService.updateCompany(eq(1L), any(CompanyCreateDto.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/companies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nowa Nazwa"));
    }

    @Test
    @WithMockUser
    void deleteCompany_ShouldReturn204NoContent() throws Exception {
        doNothing().when(companyService).deleteCompany(1L);

        mockMvc.perform(delete("/api/companies/1"))
                .andExpect(status().isNoContent());
    }
}

