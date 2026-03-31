package com.bivolaris.backend_capstone_project.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFoundException(EmployeeNotFoundException e){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().format(formatter));
        errorBody.put("status", HttpStatus.NOT_FOUND.value());
        errorBody.put("error", "Employee not found");
        errorBody.put("message", e.getMessage());
        errorBody.put("requestType", "GET");
        errorBody.put("path", "/employees/{employeeId}");



        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeesNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeesNotFoundException(EmployeesNotFoundException e){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().format(formatter));
        errorBody.put("status", HttpStatus.NOT_FOUND.value());
        errorBody.put("error", "Employees not found");
        errorBody.put("message", e.getMessage());
        errorBody.put("requestType", "GET");
        errorBody.put("path", "/employees");

        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeCreationException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeCreationException(EmployeeCreationException e){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().format(formatter));
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error", "Cannot create employee, check data.");
        errorBody.put("message", e.getMessage());
        errorBody.put("requestType", "POST");
        errorBody.put("path", "/employees");

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmployeeUpdateException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeUpdateException(EmployeeUpdateException e){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().format(formatter));
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error", "Cannot update employee, check data: employeeId and body data.");
        errorBody.put("message", e.getMessage());
        errorBody.put("requestType", "PUT");
        errorBody.put("path", "/employees/{employeeId}");

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EmployeeDeletionException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeDeletionException(EmployeeDeletionException e){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().format(formatter));
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error", "Cannot delete employee, check data: employeeId.");
        errorBody.put("message", e.getMessage());
        errorBody.put("requestType", "DELETE");

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, String>> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, String> fieldError = new HashMap<>();
                    fieldError.put("field", error.getField());
                    fieldError.put("message", error.getDefaultMessage());
                    return fieldError;
                })
                .toList();

        response.put("timestamp", LocalDateTime.now().format(formatter));
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation failed");
        response.put("message", "Invalid fields in request");
        response.put("errors", validationErrors);
        response.put("path", request.getRequestURI());
        response.put("requestType", request.getMethod());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().format(formatter));
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error", "Malformed JSON request");
        errorBody.put("message", "Request body is missing or invalid JSON");
        errorBody.put("path", request.getRequestURI());
        errorBody.put("requestType", request.getMethod());

        return ResponseEntity.badRequest().body(errorBody);
    }







}
