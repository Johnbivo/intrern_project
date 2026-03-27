package com.bivolaris.myapplication.controller;

import com.bivolaris.myapplication.model.CreateDeviceRequest;
import com.bivolaris.myapplication.model.Device;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeviceController {


    @GET("devices")
    Call<List<Device>> getDevices();

    @GET("devices/{serialNumber}")
    Call<Device> getDevice(@Path("serialNumber") String serialNumber);

    @POST("devices")
    Call<Void> createDevice(@Body CreateDeviceRequest device);

    @PUT("devices/{serialNumber}")
    Call<Device> updateDevice(@Path("serialNumber") String serialNumber,@Body Device device);

    @DELETE("devices/{serialNumber}")
    Call<Void> deleteDevice(@Path("serialNumber") String serialNumber);

    @POST("devices/assign/{serialNumber}/company/{companyId}")
    Call<Void> assignDeviceToCompany(@Path("serialNumber") String serialNumber,@Path("companyId") String companyId);

    @POST("devices/assign/{serialNumber}/employee/{employeeId}")
    Call<Void> assignDeviceToEmployee(@Path("serialNumber") String serialNumber,@Path("employeeId") String employeeId);

    @POST("devices/unassign/{serialNumber}/company/{companyId}")
    Call<Void> unassignDeviceFromCompany(@Path("serialNumber") String serialNumber,@Path("companyId") String companyId);

    @POST("devices/unassign/{serialNumber}/employee/{employeeId}")
    Call<Void> unassignDeviceFromEmployee(@Path("serialNumber") String serialNumber,@Path("employeeId") String employeeId);



    @GET("devices/company/{companyId}")
    Call<List<Device>> getDevicesByCompanyId(@Path("companyId") String companyId);

}
