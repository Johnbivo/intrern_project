package com.bivolaris.backend_capstone_project.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateEmployeeRequest {

    @NotEmpty( message = "Employee name is required")
    private String name;

    @NotEmpty( message = "Employee email is required")
    @Email( message = "Employee email is not valid")
    private String email;


    private String companyId;
}
