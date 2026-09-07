package com.gateflow.GateFlow.service;
import com.gateflow.GateFlow.dto.CompanyCreateDto;
import com.gateflow.GateFlow.dto.CompanyDto;

import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void findAll_ShouldReturnListOfCompanyDtos() {
        Company company1 = new Company();
        company1.setId(1L);
        company1.setName("Firma A");

        Company company2 = new Company();
        company2.setId(2L);
        company2.setName("Firma B");

        when(companyRepository.findAll()).thenReturn(List.of(company1, company2));

        List<CompanyDto> result = companyService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Firma A", result.get(0).name());
        assertEquals("Firma B", result.get(1).name());
        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnCompanyDto_WhenCompanyExists() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Firma A");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        CompanyDto result = companyService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Firma A", result.name());
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenCompanyDoesNotExist() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> companyService.findById(1L));

        assertEquals("Nie znaleziono firmy o ID: 1", exception.getMessage());
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void addCompany_ShouldCreateAndReturnCompanyDto_WhenNameIsUnique() {
        CompanyCreateDto createDto = new CompanyCreateDto("Firma A");
        Company savedCompany = new Company();
        savedCompany.setId(1L);
        savedCompany.setName("Firma A");

        when(companyRepository.findByNameIgnoreCase("Firma A")).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        CompanyDto result = companyService.addCompany(createDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Firma A", result.name());
        verify(companyRepository, times(1)).findByNameIgnoreCase("Firma A");
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void addCompany_ShouldThrowException_WhenCompanyWithNameAlreadyExists() {
        CompanyCreateDto createDto = new CompanyCreateDto("Firma A");
        Company existingCompany = new Company();
        existingCompany.setId(1L);
        existingCompany.setName("Firma A");

        when(companyRepository.findByNameIgnoreCase("Firma A")).thenReturn(Optional.of(existingCompany));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> companyService.addCompany(createDto));

        assertEquals("Firma o nazwie Firma A istnieje", exception.getMessage());
        verify(companyRepository, times(1)).findByNameIgnoreCase("Firma A");
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void deleteCompany_ShouldDelete_WhenCompanyExists() {
        when(companyRepository.existsById(1L)).thenReturn(true);
        doNothing().when(companyRepository).deleteById(1L);

        companyService.deleteCompany(1L);

        verify(companyRepository, times(1)).existsById(1L);
        verify(companyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCompany_ShouldThrowException_WhenCompanyDoesNotExist() {
        when(companyRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> companyService.deleteCompany(1L));

        assertEquals("Nie znaleziono firmy o ID: 1", exception.getMessage());
        verify(companyRepository, times(1)).existsById(1L);
        verify(companyRepository, never()).deleteById(1L);
    }

    @Test
    void updateCompany_ShouldUpdateAndReturnCompanyDto_WhenCompanyExists() {
        CompanyCreateDto updateDto = new CompanyCreateDto("Nowa Nazwa");
        Company existingCompany = new Company();
        existingCompany.setId(1L);
        existingCompany.setName("Stara Nazwa");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));

        CompanyDto result = companyService.updateCompany(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Nowa Nazwa", result.name());
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void updateCompany_ShouldThrowException_WhenCompanyDoesNotExist() {
        CompanyCreateDto updateDto = new CompanyCreateDto("Nowa Nazwa");

        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> companyService.updateCompany(1L, updateDto));

        assertEquals("Nie znaleziono firmy o ID: 1", exception.getMessage());
        verify(companyRepository, times(1)).findById(1L);
    }
}


