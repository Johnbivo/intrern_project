package com.bivolaris.backend_capstone_project.repositories;


import com.bivolaris.backend_capstone_project.entities.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    List<Employee> findAllByCompanyId(String companyId);
}
