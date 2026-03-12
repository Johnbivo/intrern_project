package com.bivolaris.backend_capstone_project.controllers;


import com.bivolaris.backend_capstone_project.dtos.CreateEmployeeRequest;
import com.bivolaris.backend_capstone_project.dtos.EmployeeDto;
import com.bivolaris.backend_capstone_project.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    // Exceptions and status codes are implemented in the global exception handler in the exceptions folder.

    private final EmployeeService employeeService;

    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeesByCompany(@PathVariable String companyId){
        return ResponseEntity.ok(employeeService.getAllCompanyEmployees(companyId));
    }

    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest){
        employeeService.createEmployee(createEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String employeeId, @RequestBody @Valid EmployeeDto employeeDto){
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }


    // unassign

    @PostMapping("/company/{companyId}/employees")
    public ResponseEntity<Void> unassignAllEmployeesFromCompany(@PathVariable String companyId){
        employeeService.unassignAllEmployeesFromCompany(companyId);
        return ResponseEntity.ok().build();
    }





    @PostMapping("/{employeeId}/company/unassignFrom/{companyId}")
    public ResponseEntity<Void> unassignEmployeeFromCompany(@PathVariable String employeeId, @PathVariable String companyId){
        if(!employeeService.unassignEmployeeFromCompany(employeeId, companyId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }







}
