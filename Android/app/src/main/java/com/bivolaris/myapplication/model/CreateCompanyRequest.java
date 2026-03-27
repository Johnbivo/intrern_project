package com.bivolaris.myapplication.model;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CreateCompanyRequest {
    private String name;
    private String address;
}
