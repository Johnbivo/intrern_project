package com.bivolaris.backend_capstone_project.dtos;


import lombok.Data;

@Data
public class DeviceDto {

    private String serialNumber;
    private String name;
    private String type;
    private String companyId;
    private String employeeId;

}
