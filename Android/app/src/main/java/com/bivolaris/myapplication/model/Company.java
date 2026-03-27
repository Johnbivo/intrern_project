package com.bivolaris.myapplication.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Company {

    private String id;
    private String name;
    private String address;

}
