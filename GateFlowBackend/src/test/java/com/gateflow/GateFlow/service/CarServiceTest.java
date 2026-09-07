package com.gateflow.GateFlow.service;




import com.gateflow.GateFlow.dto.CarDto;
import com.gateflow.GateFlow.model.Car;
import com.gateflow.GateFlow.model.Company;
import com.gateflow.GateFlow.repository.CarRepository;
import com.gateflow.GateFlow.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CarService carService;

    private Car carWithCompany;
    private Car carWithoutCompany;
    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(10L);
        company.setName("Logistics Corp");

        carWithCompany = new Car();
        carWithCompany.setId(1L);
        carWithCompany.setRegistrationNumber("SK12345");
        carWithCompany.setBrand("BMW");
        carWithCompany.setActive(true);
        carWithCompany.setCompany(company);

        carWithoutCompany = new Car();
        carWithoutCompany.setId(2L);
        carWithoutCompany.setRegistrationNumber("KR99999");
        carWithoutCompany.setBrand("Audi");
        carWithoutCompany.setActive(false);
        carWithoutCompany.setCompany(null);
    }



    @Test

    void findAll_shouldReturnListOfCarDtos() {

        when(carRepository.findAll()).thenReturn(List.of(carWithCompany, carWithoutCompany));


        List<CarDto> result = carService.findAll();


        assertThat(result).hasSize(2);
        assertThat(result.get(0).companyName()).isEqualTo("Logistics Corp");
        assertThat(result.get(1).companyName()).isEqualTo("brak");
        verify(carRepository, times(1)).findAll();
    }

    @Test

    void findById_shouldReturnCarDto_whenCarExists() {

        when(carRepository.findById(1L)).thenReturn(Optional.of(carWithCompany));


        CarDto result = carService.findById(1L);


        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.registrationNumber()).isEqualTo("SK12345");
        assertThat(result.brand()).isEqualTo("BMW");
        assertThat(result.companyName()).isEqualTo("Logistics Corp");
    }

    @Test

    void findById_shouldThrowException_whenCarDoesNotExist() {

        when(carRepository.findById(99L)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> carService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nie znaleziono auta o ID: 99");
    }

    @Test

    void findByRegistrationNumber_shouldReturnCarDto_whenFound() {

        when(carRepository.findByRegistrationNumberIgnoreCase("sk12345"))
                .thenReturn(Optional.of(carWithCompany));


        CarDto result = carService.findByRegistrationNumber("sk12345");


        assertThat(result.registrationNumber()).isEqualTo("SK12345");
    }

    @Test

    void findByRegistrationNumber_shouldThrowException_whenNotFound() {

        when(carRepository.findByRegistrationNumberIgnoreCase("NO_EXIST"))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> carService.findByRegistrationNumber("NO_EXIST"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Nie znaleziono auta o podanej rejestracji");
    }

    @Test

    void findByBrand_shouldReturnCarDtos() {

        when(carRepository.findByBrandIgnoreCase("BMW")).thenReturn(List.of(carWithCompany));


        List<CarDto> result = carService.findByBrand("BMW");


        assertThat(result).hasSize(1);
        assertThat(result.get(0).brand()).isEqualTo("BMW");
    }

    @Test

    void findByCompanyName_shouldReturnCarDtos() {

        when(carRepository.findByCompanyNameIgnoreCase("Logistics Corp")).thenReturn(List.of(carWithCompany));


        List<CarDto> result = carService.findByCompanyName("Logistics Corp");


        assertThat(result).hasSize(1);
        assertThat(result.get(0).companyName()).isEqualTo("Logistics Corp");
    }



    @Test

    void addCar_shouldCreateCarWithExistingCompany() {

        CarDto inputDto = new CarDto(null, "WA11111", "Mercedes", "Logistics Corp", true);

        when(companyRepository.findByNameIgnoreCase("Logistics Corp")).thenReturn(Optional.of(company));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> {
            Car savedCar = invocation.getArgument(0);
            savedCar.setId(3L);
            return savedCar;
        });


        CarDto result = carService.addCar(inputDto);


        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.companyName()).isEqualTo("Logistics Corp");
        verify(companyRepository, times(1)).findByNameIgnoreCase("Logistics Corp");
        verify(companyRepository, never()).save(any(Company.class));
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test

    void addCar_shouldCreateNewCompany_whenCompanyNotFound() {

        CarDto inputDto = new CarDto(null, "WA11111", "Mercedes", "New Trans Ltd", true);

        Company newCompany = new Company();
        newCompany.setId(20L);
        newCompany.setName("New Trans Ltd");

        when(companyRepository.findByNameIgnoreCase("New Trans Ltd")).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenReturn(newCompany);
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> {
            Car savedCar = invocation.getArgument(0);
            savedCar.setId(4L);
            return savedCar;
        });


        CarDto result = carService.addCar(inputDto);


        assertThat(result.companyName()).isEqualTo("New Trans Ltd");
        verify(companyRepository, times(1)).save(any(Company.class));
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test

    void addCar_shouldCreateCarWithoutCompany_whenCompanyNameIsBlank() {

        CarDto inputDto = new CarDto(null, "WA11111", "Mercedes", "   ", true);

        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> {
            Car savedCar = invocation.getArgument(0);
            savedCar.setId(5L);
            return savedCar;
        });


        CarDto result = carService.addCar(inputDto);


        assertThat(result.companyName()).isEqualTo("brak");
        verify(companyRepository, never()).findByNameIgnoreCase(anyString());
    }



    @Test
    @DisplayName("updateCar - Powinno zaktualizować dane istniejącego auta")
    void updateCar_shouldUpdateExistingFields_whenCarExists() {

        CarDto updateDto = new CarDto(1L, "NEW-REG", "BMW-Updated", "Logistics Corp", false);

        when(carRepository.findById(1L)).thenReturn(Optional.of(carWithCompany));
        when(companyRepository.findByNameIgnoreCase("Logistics Corp")).thenReturn(Optional.of(company));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));


        CarDto result = carService.updateCar(1L, updateDto);


        assertThat(result.registrationNumber()).isEqualTo("NEW-REG");
        assertThat(result.brand()).isEqualTo("BMW-Updated");
        assertThat(result.active()).isFalse();
        verify(carRepository, times(1)).save(carWithCompany);
    }

    @Test

    void updateCar_shouldThrowException_whenCarDoesNotExist() {

        CarDto updateDto = new CarDto(99L, "REG", "Brand", null, true);
        when(carRepository.findById(99L)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> carService.updateCar(99L, updateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nie znaleziono auta o ID: 99");

        verify(carRepository, never()).save(any());
    }



    @Test

    void deleteCar_shouldDelete_whenCarExists() {

        when(carRepository.existsById(1L)).thenReturn(true);


        carService.deleteCar(1L);


        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test

    void deleteCar_shouldThrowException_whenCarDoesNotExist() {

        when(carRepository.existsById(99L)).thenReturn(false);


        assertThatThrownBy(() -> carService.deleteCar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Nie można usunąć. Nie znaleziono auta o ID: 99");

        verify(carRepository, never()).deleteById(anyLong());
    }
}
