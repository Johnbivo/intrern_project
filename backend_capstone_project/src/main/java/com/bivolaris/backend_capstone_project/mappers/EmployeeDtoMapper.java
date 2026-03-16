package com.bivolaris.backend_capstone_project.mappers;


import com.bivolaris.backend_capstone_project.dtos.CreateEmployeeRequest;
import com.bivolaris.backend_capstone_project.dtos.EmployeeDto;
import com.bivolaris.backend_capstone_project.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeDtoMapper {

    EmployeeDto toDto(Employee employee);
    Employee toEntity(EmployeeDto employeeDto);


    @Mapping(target = "id", ignore = true)
    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);
}
