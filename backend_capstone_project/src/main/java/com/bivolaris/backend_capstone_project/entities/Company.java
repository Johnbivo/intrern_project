package com.bivolaris.backend_capstone_project.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "companies")
public class Company {

    @Id
    private String id;


    private String name;
    private String address;


}
