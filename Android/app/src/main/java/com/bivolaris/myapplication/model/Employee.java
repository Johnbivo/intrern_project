package com.bivolaris.myapplication.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class Employee {

    private String id;
    private String name;
    private String email;
    private String companyId;
}
