package com.bivolaris.backend_capstone_project.controllers;


import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;
import com.bivolaris.backend_capstone_project.services.CompanyService;
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
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        if(companyService.getAllCompanies() == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable String id) {
        if (companyService.getCompanyById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping
    private ResponseEntity<Void> createCompany(@RequestBody @Valid CreateCompanyRequest createCompanyRequest) {
        if(companyService.createCompany(createCompanyRequest))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<CompanyDto> updateCompany(@PathVariable String id,@RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompany(id, companyDto);
        if(updatedCompany == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        if(!(companyService.deleteCompany(id)))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();

    }


}
