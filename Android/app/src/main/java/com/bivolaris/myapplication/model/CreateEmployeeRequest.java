package com.bivolaris.myapplication.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEmployeeRequest {

    private String name;
    private String email;
    private String companyId;
}
