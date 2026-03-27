package com.bivolaris.myapplication.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateDeviceRequest {

    private String serialNumber;
    private String name;
    private String type;
    private String companyId;

}
