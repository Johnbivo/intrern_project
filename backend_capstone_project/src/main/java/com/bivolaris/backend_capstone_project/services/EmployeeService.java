package com.bivolaris.backend_capstone_project.services;

import com.bivolaris.backend_capstone_project.dtos.CreateEmployeeRequest;
import com.bivolaris.backend_capstone_project.dtos.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeById(String id);
    boolean createEmployee(CreateEmployeeRequest createEmployeeRequest);
    EmployeeDto updateEmployee(String id, EmployeeDto employeeDto);
    boolean deleteEmployee(String id);

    List<EmployeeDto> getAllCompanyEmployees(String companyId);
    boolean unassignEmployeeFromCompany(String employeeId, String companyId);
    void unassignAllEmployeesFromCompany(String companyId);


}
