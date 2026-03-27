package com.bivolaris.myapplication.controller;

import com.bivolaris.myapplication.model.CreateEmployeeRequest;
import com.bivolaris.myapplication.model.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmployeeController {

    @GET("employees")
    Call<List<Employee>> getEmployees();

    @GET("employees/{id}")
    Call<Employee> getEmployeeById(@Path("id") String id);

    @GET("employees/company/{id}")
    Call<List<Employee>> getEmployeesByCompanyId(@Path("id") String id);

    @POST("employees")
    Call<Void> createEmployee(@Body CreateEmployeeRequest createEmployeeRequest);

    @PUT("employees/{id}")
    Call<Employee> updateEmployee(@Path("id") String id,@Body Employee employee);

    @DELETE("employees/{id}")
    Call<Void> deleteEmployee(@Path("id") String id);
}
