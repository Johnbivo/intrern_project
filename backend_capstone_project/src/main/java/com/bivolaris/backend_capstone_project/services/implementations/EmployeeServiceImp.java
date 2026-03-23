package com.bivolaris.backend_capstone_project.services.implementations;


import com.bivolaris.backend_capstone_project.dtos.CreateEmployeeRequest;
import com.bivolaris.backend_capstone_project.dtos.EmployeeDto;
import com.bivolaris.backend_capstone_project.entities.Employee;
import com.bivolaris.backend_capstone_project.exceptions.*;
import com.bivolaris.backend_capstone_project.mappers.EmployeeDtoMapper;
import com.bivolaris.backend_capstone_project.repositories.DeviceRepository;
import com.bivolaris.backend_capstone_project.repositories.EmployeeRepository;
import com.bivolaris.backend_capstone_project.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;


@Primary
@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeDtoMapper employeeDtoMapper;
    private final DeviceRepository deviceRepository;


    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> employees =  employeeRepository.findAll().stream()
                .map(employeeDtoMapper::toDto)
                .toList();
        if (employees.isEmpty()){
            throw new EmployeesNotFoundException("No employees found.");
        }
        return employees;
    }

    @Override
    public EmployeeDto getEmployeeById(String id) {
        EmployeeDto employee = employeeRepository.findById(id).map(employeeDtoMapper::toDto).orElse(null);
        if (employee == null){
            throw new EmployeeNotFoundException("Employee not found.");
        }
        return employee;
    }


    @Override
    public boolean createEmployee(CreateEmployeeRequest createEmployeeRequest){

        try {
            Employee employee =  employeeDtoMapper.toEntity(createEmployeeRequest);
            employeeRepository.save(employee);
            return true;
        } catch (Exception e){
            throw new EmployeeCreationException("Employee creation failed.");
        }
    }


    @Override
    public EmployeeDto updateEmployee(String id, EmployeeDto employeeDto){
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null){
            throw new EmployeeNotFoundException("This employee does not exist.");
        }

        try{
            employee = employeeDtoMapper.toEntity(employeeDto);
            employeeRepository.save(employee);
        }catch (Exception e){
            throw new EmployeeUpdateException("Employee update failed.");
        }
        return employeeDtoMapper.toDto(employee);
    }

    @Override
    public boolean deleteEmployee(String id){
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null){
            throw new EmployeeNotFoundException("This employee does not exist.");
        }
        try {
            var employeeId = employee.getId();

            deviceRepository.findAllByEmployeeId(employeeId).forEach(device -> {
                device.setEmployeeId(null);
                deviceRepository.save(device);
            });

            employeeRepository.deleteById(id);
        }catch (Exception e){
            throw new EmployeeDeletionException("Employee deletion failed.");
        }
        return true;

    }


    @Override
    public List<EmployeeDto> getAllCompanyEmployees(String companyId){
        List<EmployeeDto> employees = employeeRepository.findAllByCompanyId(companyId)
                .stream()
                .map(employeeDtoMapper::toDto)
                .toList();
        if (employees.isEmpty()){
            throw new EmployeesNotFoundException("No employees found for this company.");
        }
        return employees;
    }


    @Override
    public boolean unassignEmployeeFromCompany(String employeeId, String companyId){
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null){
            return false;
        }
        employee.setCompanyId(null);
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public void unassignAllEmployeesFromCompany(String companyId){
        List<Employee> employees = employeeRepository.findAllByCompanyId(companyId);
        for (Employee employee : employees){
            employee.setCompanyId(null);
            employeeRepository.save(employee);
        }
    }




}
