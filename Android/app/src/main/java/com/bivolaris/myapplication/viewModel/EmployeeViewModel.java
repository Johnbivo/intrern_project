package com.bivolaris.myapplication.viewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bivolaris.myapplication.config.RetrofitClient;
import com.bivolaris.myapplication.controller.EmployeeController;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateEmployeeRequest;
import com.bivolaris.myapplication.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class EmployeeViewModel extends ViewModel {

    private final MutableLiveData<List<Employee>> employees = new MutableLiveData<>();

    public LiveData<List<Employee>> getEmployees() {
        return employees;
    }

    // constructor only for testing purposes, in prod this should be deleted or commended out
    public MutableLiveData<List<Employee>> getMutableEmployees() {
        return employees;
    }


    public EmployeeViewModel() { this(null); }

    private final EmployeeController controller;
    public EmployeeViewModel(EmployeeController controller){
        this.controller = (controller != null) ? controller : RetrofitClient.getInstance().getEmployeeController();
    }

    public void loadEmployees(){
        controller.getEmployees()
                .enqueue(new Callback<List<Employee>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            employees.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to load employees");
                    }
                });
    }

    public void loadEmployeesByCompany(String companyId){
        // Set the value to null to avoid showing the previous list of employees
        employees.setValue(null);
        controller.getEmployeesByCompanyId(companyId)
                .enqueue(new Callback<List<Employee>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            employees.setValue(response.body());
                        } else {
                            // Reset the value to an empty list if the request fails
                            employees.setValue(new ArrayList<>());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to load employees by company");
                        employees.setValue(new ArrayList<>());
                    }
                });
    }



    public void createEmployee(CreateEmployeeRequest createEmployeeRequest){
        controller.createEmployee(createEmployeeRequest)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            loadEmployees();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to create employee");
                    }
                });
    }

    public void updateEmployee(Employee employee){
        controller.updateEmployee(employee.getId(), employee)
                .enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Employee updatedEmployee = response.body();

                            List<Employee> updatedList = employees.getValue()
                                    .stream()
                                    .map(employee -> employee.getId().equals(updatedEmployee.getId()) ? updatedEmployee : employee)
                                    .collect(Collectors.toList());
                            employees.setValue(updatedList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to update employee");
                    }
                });
    }

    public void deleteEmployee(String id){
        controller.deleteEmployee(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            List<Employee> currentList = new ArrayList<>(employees.getValue());
                            currentList.removeIf(employee -> employee.getId().equals(id));
                            employees.setValue(currentList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to delete employee");
                    }
                });
    }


}
