package com.bivolaris.backend_capstone_project.services;


import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;



import java.util.List;

public interface CompanyService {

    List<CompanyDto> getAllCompanies();
    CompanyDto getCompanyById(String id);
    boolean createCompany(CreateCompanyRequest createCompanyRequest);
    CompanyDto updateCompany(String id, CompanyDto companyDto);
    boolean deleteCompany(String id);




}
