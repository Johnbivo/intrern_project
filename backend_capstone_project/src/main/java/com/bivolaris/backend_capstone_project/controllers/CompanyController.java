package com.bivolaris.backend_capstone_project.controllers;


import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;
import com.bivolaris.backend_capstone_project.services.CompanyService;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
@Tag(name = "Company", description = "The Company API")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    @Operation(summary = "Get all companies", description = "Retrieves a list of all companies")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @ApiResponse(responseCode = "404", description = "No companies found")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        if(companyService.getAllCompanies() == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID", description = "Retrieves a single company by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved company")
    @ApiResponse(responseCode = "404", description = "Company not found")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable String id) {
        if (companyService.getCompanyById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new company", description = "Creates a new company based on the provided request body")
    @ApiResponse(responseCode = "201", description = "Successfully created company")
    @ApiResponse(responseCode = "400", description = "Invalid input or company already exists")
    private ResponseEntity<Void> createCompany(@RequestBody @Valid CreateCompanyRequest createCompanyRequest) {
        if(companyService.createCompany(createCompanyRequest))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing company", description = "Updates a company's details by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated company")
    @ApiResponse(responseCode = "400", description = "Invalid input or update failed")
    private ResponseEntity<CompanyDto> updateCompany(@PathVariable String id,@RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompany(id, companyDto);
        if(updatedCompany == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a company", description = "Deletes a company by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully deleted company")
    @ApiResponse(responseCode = "404", description = "Company not found")
    private ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        if(!(companyService.deleteCompany(id)))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();

    }


}
