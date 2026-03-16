package com.bivolaris.backend_capstone_project.mappers;

import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;
import com.bivolaris.backend_capstone_project.entities.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyDtoMapper {

    CompanyDto toDto(Company company);
    Company toEntity(CompanyDto companyDto);

    @Mapping(target = "id", ignore = true)
    Company toEntity(CreateCompanyRequest createCompanyRequest);
}
