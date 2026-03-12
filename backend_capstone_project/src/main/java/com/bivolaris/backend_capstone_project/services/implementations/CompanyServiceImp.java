package com.bivolaris.backend_capstone_project.services.implementations;


import com.bivolaris.backend_capstone_project.dtos.CompanyDto;
import com.bivolaris.backend_capstone_project.dtos.CreateCompanyRequest;
import com.bivolaris.backend_capstone_project.entities.Company;
import com.bivolaris.backend_capstone_project.mappers.CompanyDtoMapper;
import com.bivolaris.backend_capstone_project.repositories.CompanyRepository;
import com.bivolaris.backend_capstone_project.services.CompanyService;
import com.bivolaris.backend_capstone_project.services.DeviceService;
import com.bivolaris.backend_capstone_project.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class CompanyServiceImp implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyDtoMapper companyDtoMapper;
    private final EmployeeService employeeService;
    private final DeviceService deviceService;

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(companyDtoMapper::toDto)
                .toList();
    }


    @Override
    public CompanyDto getCompanyById(String id) {

        return companyRepository.findById(id).map(companyDtoMapper::toDto).orElse(null);
    }

    @Override
    public boolean createCompany(CreateCompanyRequest createCompanyRequest) {
        var companyName = createCompanyRequest.getName();
        if (companyName == null || companyName.isEmpty()) {
            return false;
        }

        if (companyExists(companyName)) {
            return false;
        }
        Company company = companyDtoMapper.toEntity(createCompanyRequest);
        
        companyRepository.save(company);
        return true;
    }


    @Override
    public CompanyDto updateCompany(String id, CompanyDto companyDto) {
        if (companyRepository.findById(id).isEmpty()){
            return null;
        }
        Company company = companyDtoMapper.toEntity(companyDto);
        company.setId(id);
        companyRepository.save(company);
        return companyDtoMapper.toDto(company);
    }


    @Override
    public boolean deleteCompany(String id) {
        if (companyRepository.findById(id).isEmpty()) {
            return false;
        }
        try {
            employeeService.unassignAllEmployeesFromCompany(id);
            deviceService.unassignAllDevicesFromCompany(id);
            companyRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }









    private boolean companyExists(String companyName) {
        return companyRepository.existsByName(companyName);
    }
}
