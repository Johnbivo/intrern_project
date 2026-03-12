package com.bivolaris.backend_capstone_project.testServices;


import com.bivolaris.backend_capstone_project.dtos.CreateEmployeeRequest;
import com.bivolaris.backend_capstone_project.dtos.EmployeeDto;
import com.bivolaris.backend_capstone_project.entities.Employee;
import com.bivolaris.backend_capstone_project.mappers.EmployeeDtoMapper;
import com.bivolaris.backend_capstone_project.repositories.DeviceRepository;
import com.bivolaris.backend_capstone_project.repositories.EmployeeRepository;
import com.bivolaris.backend_capstone_project.services.implementations.EmployeeServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeesServiceImpTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeDtoMapper employeeDtoMapper;

    @Mock
    private DeviceRepository deviceRepository;



    @InjectMocks
    private EmployeeServiceImp employeeService;

    private Employee testEmployee;
    private EmployeeDto testEmployeeDto;
    private CreateEmployeeRequest testCreateEmployeeRequest;



    @BeforeEach
    void setUp() {


        testEmployee = new Employee();
        testEmployee.setId("123");
        testEmployee.setName("Test Employee");
        testEmployee.setEmail("testmail@mail.com");
        testEmployee.setCompanyId("69aac28e9ac973972526e158");

        testEmployeeDto = new EmployeeDto();
        testEmployeeDto.setId("123");
        testEmployeeDto.setName("Test Employee");
        testEmployeeDto.setEmail("testmail@mail.com");
        testEmployeeDto.setCompanyId("69aac28e9ac973972526e158");

        testCreateEmployeeRequest = new CreateEmployeeRequest();
        testCreateEmployeeRequest.setName("Test Employee");
        testCreateEmployeeRequest.setEmail("testmail@mail.com");
        testCreateEmployeeRequest.setCompanyId("69aac28e9ac973972526e158");


    }


    @Test
    @DisplayName("Should return the employee with the coresponding id")
    void getEmployeeById_ValidId_ReturnsEmployee() {

        String id = "123";
        when(employeeRepository.findById(id)).thenReturn(Optional.of(testEmployee));
        when(employeeDtoMapper.toDto(testEmployee)).thenReturn(testEmployeeDto);

        EmployeeDto result = employeeService.getEmployeeById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(testEmployeeDto, result);

    }

    @Test
    @DisplayName("Should create an employee with a valid given createRequest")
    void createEmployee_ValidCreateRequest_ShouldCreateEmployee() {

        when(employeeDtoMapper.toEntity(testCreateEmployeeRequest)).thenReturn(testEmployee);
        boolean result = employeeService.createEmployee(testCreateEmployeeRequest);
        assert result;
        assertNotNull(testEmployee.getId());
        assertEquals("Test Employee", testEmployee.getName());
        assertEquals("testmail@mail.com", testEmployee.getEmail());
        assertEquals("69aac28e9ac973972526e158", testEmployee.getCompanyId());
    }
    
    @Test
    @DisplayName("Should update an employee with the given id and employee dto")
    void updateEmployee_ValidIdAndEmployeeDto_ShouldUpdateEmployee() {

        String employeeId = "123";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(employeeDtoMapper.toEntity(testEmployeeDto)).thenReturn(testEmployee);
        when(employeeDtoMapper.toDto(testEmployee)).thenReturn(testEmployeeDto);

        EmployeeDto result = employeeService.updateEmployee(employeeId, testEmployeeDto);

        assertNotNull(result);
        assertEquals(employeeId, result.getId());
        assertEquals(testEmployeeDto, result);

        verify(employeeRepository).save(testEmployee);

    }

    @Test
    @DisplayName("Should delete an employee with the given id")
    void deleteEmployee_ValidId_ShouldDeleteEmployee() {
        String id = "123";
        when(employeeRepository.findById(id)).thenReturn(Optional.of(testEmployee));
        when(deviceRepository.findAllByEmployeeId(id)).thenReturn(emptyList()); // unassign all devices


        boolean result = employeeService.deleteEmployee(id);

        assertTrue(result);
        verify(employeeRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should return all company employees by a companyID")
    void getAllCompanyEmployees_ShouldReturnListOfEmployees() {
        String companyId = "123";
        List<Employee> employees = List.of(testEmployee);

        when(employeeRepository.findAllByCompanyId(companyId)).thenReturn(employees);
        when(employeeDtoMapper.toDto(testEmployee)).thenReturn(testEmployeeDto);

        List<EmployeeDto> result = employeeService.getAllCompanyEmployees(companyId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEmployeeDto, result.get(0));
        verify(employeeRepository).findAllByCompanyId(companyId);
        verify(employeeDtoMapper).toDto(testEmployee);
    }




}
