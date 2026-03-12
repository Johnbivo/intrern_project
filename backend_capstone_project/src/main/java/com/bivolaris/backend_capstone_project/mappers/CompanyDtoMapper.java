package com.bivolaris.backend_capstone_project.mappers;

import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;
import com.bivolaris.backend_capstone_project.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyDtoMapper {

    CompanyDto toDto(Company company);
    Company toEntity(CompanyDto companyDto);

    Company toEntity(CreateCompanyRequest createCompanyRequest);
}
