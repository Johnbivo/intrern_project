package com.bivolaris.backend_capstone_project.exceptions;

public class EmployeesNotFoundException extends RuntimeException {
    public EmployeesNotFoundException(String message) {
        super(message);
    }
}
