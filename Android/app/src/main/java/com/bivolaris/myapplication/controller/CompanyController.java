package com.bivolaris.myapplication.controller;

import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateCompanyRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompanyController {
    @GET("companies")
    Call<List<Company>> getCompanies();

    @GET("companies/{id}")
    Call<Company> getCompanyById(@Path("id") String id);

    @POST("companies")
    Call<Void> createCompany(@Body CreateCompanyRequest company);


    @PUT("companies/{id}")
    Call<Company> updateCompany(@Path("id") String id, @Body Company company);

    @DELETE("companies/{id}")
    Call<Void> deleteCompany(@Path("id") String id);


}


