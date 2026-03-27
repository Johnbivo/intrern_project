package com.bivolaris.myapplication.config;

import com.bivolaris.myapplication.constants.Constants;
import com.bivolaris.myapplication.controller.CompanyController;
import com.bivolaris.myapplication.controller.DeviceController;
import com.bivolaris.myapplication.controller.EmployeeController;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
public class RetrofitClient {

    private static final String BASE_URL = Constants.BASE_URL;
    private static RetrofitClient instance;
    private final CompanyController companyController;
    private final EmployeeController employeeController;
    private final DeviceController deviceController;

    private RetrofitClient(){
        Retrofit retrofitClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        companyController = retrofitClient.create(CompanyController.class);
        employeeController = retrofitClient.create(EmployeeController.class);
        deviceController = retrofitClient.create(DeviceController.class);

    }

    public static RetrofitClient getInstance(){
        if (instance == null){
            instance = new RetrofitClient();
        }
        return instance;
    }

}
