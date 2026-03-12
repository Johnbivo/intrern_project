package com.bivolaris.backend_capstone_project.dtos;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateDeviceRequest {


    public String serialNumber;

    @NotEmpty( message = "Device name is required")
    public String name;

    @NotEmpty( message = "Device type is required")
    public String type;

    @NotEmpty( message = "Device company id is required")
    public String companyId;
}
