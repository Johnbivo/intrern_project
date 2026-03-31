package com.bivolaris.myapplication.viewModel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bivolaris.myapplication.Logger;
import com.bivolaris.myapplication.config.RetrofitClient;
import com.bivolaris.myapplication.controller.CompanyController;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateCompanyRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;

public class CompaniesViewModel extends ViewModel {


    private final MutableLiveData<List<Company>> companies = new MutableLiveData<>(new ArrayList<>());

    // constructor only for testing purposes, in prod this should be deleted or commended out
    public MutableLiveData<List<Company>> getMutableCompanies() {
        return companies;
    }

    public LiveData<List<Company>> getCompanies() {
        return companies;
    }

    public CompaniesViewModel() { this(null); }


    // added for mockito testing
    private final CompanyController controller;
    public CompaniesViewModel(CompanyController controller) {
        this.controller = (controller != null) ? controller : RetrofitClient.getInstance().getCompanyController();
    }


    // finish of the controller mockito test

    public void loadCompanies(){
        controller.getCompanies()
                .enqueue(new Callback<List<Company>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Company>> call, @NonNull retrofit2.Response<List<Company>> response) {
                        if (response.isSuccessful()) {
                            companies.setValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<List<Company>> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to load companies");
                    }
                });
    }






    public void createCompany(CreateCompanyRequest request){
        controller.createCompany(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            loadCompanies();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to create company");

                    }
                });
    }



    public void updateCompany(Company updatedCompany){
        controller.updateCompany(updatedCompany.getId(), updatedCompany)
                .enqueue(new Callback<Company>() {
                    @Override
                    public void onResponse(@NonNull Call<Company> call, @NonNull retrofit2.Response<Company> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Company updatedCompany = response.body();
                            List<Company> updatedList = companies.getValue()
                                    .stream()
                                    .map(company -> company.getId()
                                            .equals(updatedCompany.getId()) ? updatedCompany : company)
                                    .collect(Collectors.toList());
                            companies.setValue(updatedList);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Company> call, @NonNull Throwable t) {
                        Timber.e(t, "Failed to update company");
                    }
                });
    }

    public void deleteCompany(Company company){
        controller.deleteCompany(company.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                        if (response.isSuccessful()) {
                            List<Company> currentList = new ArrayList<>(companies.getValue());
                            currentList.removeIf(c -> c.getId().equals(company.getId()));
                            companies.setValue(currentList);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                       Timber.e(t, "Failed to delete company");
                    }
                });
    }
}