package com.bivolaris.myapplication.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device {

    private String serialNumber;
    private String name;
    private String type;
    private String companyId;
    private String employeeId;

}
