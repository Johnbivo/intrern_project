package com.bivolaris.backend_capstone_project.controllers;


import com.bivolaris.backend_capstone_project.dtos.CreateEmployeeRequest;
import com.bivolaris.backend_capstone_project.dtos.EmployeeDto;
import com.bivolaris.backend_capstone_project.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Employee", description = "The Employee API")
public class EmployeeController {

    // Exceptions and status codes are implemented in the global exception handler in the exceptions folder.

    private final EmployeeService employeeService;

    @GetMapping()
    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves a single employee by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved employee")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all employees by company ID", description = "Retrieves a list of employees belonging to a specific company")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeesByCompany(@PathVariable String companyId){
        return ResponseEntity.ok(employeeService.getAllCompanyEmployees(companyId));
    }

    @PostMapping
    @Operation(summary = "Create a new employee", description = "Creates a new employee based on the provided request body")
    @ApiResponse(responseCode = "201", description = "Successfully created employee")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Void> createEmployee(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest){
        employeeService.createEmployee(createEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{employeeId}")
    @Operation(summary = "Update an existing employee", description = "Updates an employee's details by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated employee")
    @ApiResponse(responseCode = "400", description = "Invalid input or update failed")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String employeeId, @RequestBody @Valid EmployeeDto employeeDto){
        EmployeeDto updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    @Operation(summary = "Delete an employee", description = "Deletes an employee by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully deleted employee")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId){
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }


    // unassign

    @PostMapping("/company/{companyId}/employees")
    @Operation(summary = "Unassign all employees from a company", description = "Removes the company assignment from all employees belonging to the specified company")
    @ApiResponse(responseCode = "200", description = "Successfully unassigned all employees")
    public ResponseEntity<Void> unassignAllEmployeesFromCompany(@PathVariable String companyId){
        employeeService.unassignAllEmployeesFromCompany(companyId);
        return ResponseEntity.ok().build();
    }





    @PostMapping("/{employeeId}/company/unassignFrom/{companyId}")
    @Operation(summary = "Unassign an employee from a company", description = "Removes the company assignment from a specific employee")
    @ApiResponse(responseCode = "200", description = "Successfully unassigned employee")
    @ApiResponse(responseCode = "400", description = "Employee not assigned to this company or unassignment failed")
    public ResponseEntity<Void> unassignEmployeeFromCompany(@PathVariable String employeeId, @PathVariable String companyId){
        if(!employeeService.unassignEmployeeFromCompany(employeeId, companyId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }







}
