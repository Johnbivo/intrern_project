package com.bivolaris.backend_capstone_project.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCompanyRequest {

    @NotEmpty( message = "Company name is required")
    private String name;

    @NotEmpty( message = "Company address is required")
    private String address;
}
