package com.bivolaris.backend_capstone_project.testServices;


import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;
import com.bivolaris.backend_capstone_project.entities.Company;
import com.bivolaris.backend_capstone_project.mappers.CompanyDtoMapper;
import com.bivolaris.backend_capstone_project.repositories.CompanyRepository;
import com.bivolaris.backend_capstone_project.services.DeviceService;
import com.bivolaris.backend_capstone_project.services.EmployeeService;
import com.bivolaris.backend_capstone_project.services.implementations.CompanyServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImpTest {


    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyDtoMapper companyDtoMapper;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private CompanyServiceImp companyService;

    private Company testCompany;
    private CompanyDto testCompanyDto;
    private CreateCompanyRequest testCreateCompanyRequest;


    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId("1");
        testCompany.setName("testCompany");
        testCompany.setAddress("testAddress");

        testCompanyDto = new CompanyDto();
        testCompanyDto.setId("1");
        testCompanyDto.setName("testCompany");
        testCompanyDto.setAddress("testAddress");

        testCreateCompanyRequest = new CreateCompanyRequest();
        testCreateCompanyRequest.setName("testCompany");
        testCreateCompanyRequest.setAddress("testAddress");
    }


    @Test
    @DisplayName("Should return a list of companyDtos")
    void getAllCompanies_ShouldReturnListOfCompanyDtos() {

        List<Company> companies = List.of(testCompany);

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyDtoMapper.toDto(testCompany)).thenReturn(testCompanyDto);

        List<CompanyDto> result = companyService.getAllCompanies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("testCompany", result.get(0).getName());
        assertEquals("testAddress", result.get(0).getAddress());
    }


    @Test
    @DisplayName("Should create a company with a valid given createRequest")
    void createCompany_ValidCreateRequest_ShouldCreateCompany() {

        when(companyDtoMapper.toEntity(testCreateCompanyRequest)).thenReturn(testCompany);


        boolean result = companyService.createCompany(testCreateCompanyRequest);


        assert result;
        assertNotNull(testCompany.getId());
        assertEquals("testCompany", testCompany.getName());
        assertEquals("testAddress", testCompany.getAddress());
    }

    @Test
    @DisplayName("Should update a company with the given id and company dto")
    void updateCompany_ValidIdAndCompanyDto_ShouldUpdateCompany() {
        String id = "1";



        when(companyRepository.findById(id)).thenReturn(Optional.of(testCompany));
        when(companyDtoMapper.toEntity(testCompanyDto)).thenReturn(testCompany);
        when(companyDtoMapper.toDto(testCompany)).thenReturn(testCompanyDto);
        CompanyDto result = companyService.updateCompany(id, testCompanyDto);


        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("testCompany", result.getName());
        assertEquals("testAddress", result.getAddress());
    }

    @Test
    @DisplayName("Should delete a company with the given id")
    void deleteCompany_ValidId_ShouldDeleteCompany() {
        String id = "1";
        when(companyRepository.findById(id)).thenReturn(Optional.of(testCompany));

        boolean result = companyService.deleteCompany(id);

        assertTrue(result);
        verify(employeeService).unassignAllEmployeesFromCompany(id);
        verify(deviceService).unassignAllDevicesFromCompany(id);
        verify(companyRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should return a company with the coresponding id")
    void getCompanyById_ValidId_ReturnsCompany() {
        String id = "1";


        when(companyRepository.findById(id)).thenReturn(Optional.of(testCompany));
        when(companyDtoMapper.toDto(testCompany)).thenReturn(testCompanyDto);


        CompanyDto result = companyService.getCompanyById(id);


        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("testCompany", result.getName());
        assertEquals("testAddress", result.getAddress());
    }


}
