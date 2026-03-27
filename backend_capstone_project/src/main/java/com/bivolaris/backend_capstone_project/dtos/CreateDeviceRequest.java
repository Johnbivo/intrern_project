package com.bivolaris.backend_capstone_project.dtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateDeviceRequest {


    private String serialNumber;

    @NotEmpty( message = "Device name is required")
    private String name;

    @NotEmpty( message = "Device type is required")
    private String type;

    @NotEmpty( message = "Device company id is required")
    private String companyId;
}
