package com.bivolaris.backend_capstone_project.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "devices")
public class Device {

    @Id
    private String serialNumber;

    private String name;
    private String type;
    private String companyId;
    private String employeeId;



}
