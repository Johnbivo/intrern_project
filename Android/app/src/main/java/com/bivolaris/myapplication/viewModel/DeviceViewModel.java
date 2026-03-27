package com.bivolaris.myapplication.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bivolaris.myapplication.config.RetrofitClient;
import com.bivolaris.myapplication.controller.DeviceController;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateDeviceRequest;
import com.bivolaris.myapplication.model.Device;
import com.bivolaris.myapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DeviceViewModel extends ViewModel {

    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>();

    public LiveData<List<Device>> getDevices(){
        return devices;
    }

    // constructor only for testing purposes, in prod this should be deleted or commended out
    public MutableLiveData<List<Device>> getMutableDevices() {
        return devices;
    }

    private final DeviceController controller;
    public DeviceViewModel(DeviceController controller) {
        this.controller = (controller != null) ? controller : RetrofitClient.getInstance().getDeviceController();

    }

    public void loadDevices(){
        controller.getDevices()
                .enqueue(new Callback<List<Device>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Device>> call,@NonNull Response<List<Device>> response) {

                        if (response.isSuccessful() && response.body() != null)
                            devices.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Device>> call,@NonNull Throwable t) {
                        Timber.e(t, "Failed to load devices");
                    }

                });

    }



    public void loadDevicesByCompany(String companyId){
        controller.getDevicesByCompanyId(companyId)
                .enqueue(new Callback<List<Device>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Device>> call,@NonNull Response<List<Device>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            devices.setValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Device>> call,@NonNull Throwable t) {
                        Timber.e(t, "Failed to load devices by company");
                    }
                });
    }

    public void createDevice(CreateDeviceRequest device){
        controller.createDevice(device)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            loadDevices();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                        Timber.e(throwable, "Failed to create device");
                    }
                });
    }
    public void updateDevice(Device device){
        controller.updateDevice(device.getSerialNumber(), device)
                .enqueue(new Callback<Device>() {
                    @Override
                    public void onResponse(@NonNull Call<Device> call, @NonNull Response<Device> response) {
                        if (response.isSuccessful()){
                            Device updatedDevice = response.body();
                            List<Device> updatedDeviceList = devices.getValue()
                                    .stream()
                                    .map(device ->
                                        device.getSerialNumber()
                                                .equals(updatedDevice.getSerialNumber()) ? updatedDevice : device)
                                    .collect(Collectors.toList());
                            devices.setValue(updatedDeviceList);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Device> call, @NonNull Throwable throwable) {
                        Timber.e(throwable, "Failed to update device");
                    }
                });
    }
    public void deleteDevice(String serialNumber){
        controller.deleteDevice(serialNumber)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            List<Device> currentList = new ArrayList<>(devices.getValue());
                            currentList.removeIf(c -> c.getSerialNumber().equals(serialNumber));
                            devices.setValue(currentList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                       Timber.e(throwable, "Failed to delete device");
                    }
                });
    }


    public void assignDeviceToCompany(String serialNumber, String companyId){
        controller.assignDeviceToCompany(serialNumber, companyId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()){
                            loadDevices(); // backend does not return the updated device
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                        Timber.e(throwable, "Failed to assign device to company");
                    }
                });
    }

    public void unassignDeviceFromCompany(String serialNumber, String companyId){
        controller.unassignDeviceFromCompany(serialNumber, companyId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response){
                        if (response.isSuccessful()){
                            loadDevices(); // same, backend does not return the updated device
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable){
                        Timber.e(throwable, "Failed to unassign device from company");
                    }
                });

    }

    public void assignDeviceToEmployee(String serialNumber, String employeeId){
        controller.assignDeviceToEmployee(serialNumber, employeeId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response){
                        loadDevices();
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable){
                        Timber.e(throwable, "Failed to assign device to employee");
                    }
                });
    }

    public void unassignDeviceFromEmployee(String serialNumber, String employeeId){
        controller.unassignDeviceFromEmployee(serialNumber, employeeId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response){
                        loadDevices();
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable){
                        Timber.e(throwable, "Failed to unassign device from employee");
                    }
                });
    }


}
