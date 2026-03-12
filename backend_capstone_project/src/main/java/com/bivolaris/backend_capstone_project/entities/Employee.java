package com.bivolaris.backend_capstone_project.entities;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    private String name;
    @Indexed(unique = true)
    private String email;

    private String companyId;
}
