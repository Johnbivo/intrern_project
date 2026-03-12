package com.bivolaris.backend_capstone_project.repositories;


import com.bivolaris.backend_capstone_project.entities.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {


    boolean existsByName(String name);
}
