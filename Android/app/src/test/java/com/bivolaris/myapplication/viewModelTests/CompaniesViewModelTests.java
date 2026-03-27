package com.bivolaris.myapplication.viewModelTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bivolaris.myapplication.controller.CompanyController;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateCompanyRequest;
import com.bivolaris.myapplication.viewModel.CompaniesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class CompaniesViewModelTests {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    CompanyController controller;

    private CompaniesViewModel viewModel;

    @Before
    public void setup(){
        viewModel = new CompaniesViewModel(controller);
    }

    @Test
    public void testLoadCompanies(){

        // Data
        List<Company> companies = List.of(
                new Company("0", "Google", "USA")
                , new Company("1", "Apple", "USA")
                , new Company("2", "Microsoft", "USA")
        );

        // mock call
        Call<List<Company>> call = mock(Call.class);
        // mock controller call
        when(controller.getCompanies()).thenReturn(call);

        // Capture data enqueue()
        ArgumentCaptor<Callback<List<Company>>> captor = ArgumentCaptor.forClass(Callback.class);


        //act
        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(companies));


        //assert

        List<Company> result = viewModel.getCompanies().getValue();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Google", result.get(0).getName());

    }

    @Test
    public void testCreateCompany() {

        //data
        CreateCompanyRequest request = new CreateCompanyRequest("Google", "USA");

        Call<Void> call = mock(Call.class);
        when(controller.createCompany(request)).thenReturn(call);

        Call<List<Company>> getCall = mock(Call.class);
        when(controller.getCompanies()).thenReturn(getCall);


        ArgumentCaptor<Callback<Void>> captor = ArgumentCaptor.forClass(Callback.class);
        ArgumentCaptor<Callback<List<Company>>> getCaptor = ArgumentCaptor.forClass(Callback.class);


        //act
        viewModel.createCompany(request);


        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(null));
        verify(getCall).enqueue(getCaptor.capture());

        List<Company> companies = List.of(new Company("0", "Google", "USA"));
        getCaptor.getValue().onResponse(getCall, Response.success(companies));

        //assert
        List<Company> result = viewModel.getCompanies().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Google", result.get(0).getName());

    }

    @Test
    public void testUpdateCompany() {

        List<Company> initial = new ArrayList<>();
        initial.add(new Company("0", "Google", "USA"));
        initial.add(new Company("1", "Apple", "USA"));
        viewModel.getMutableCompanies().setValue(initial);


        Company updated = new Company("1", "Apple Inc", "USA");

        Call<Company> call = mock(Call.class);
        when(controller.updateCompany("1", updated)).thenReturn(call);

        ArgumentCaptor<Callback<Company>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.updateCompany(updated);

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(updated));

        List<Company> result = viewModel.getCompanies().getValue();
        assertNotNull(result);
        assertEquals("Apple Inc", result.get(1).getName());
    }

    @Test
    public void testDeleteCompany() {

        List<Company> initial = new ArrayList<>();
        initial.add(new Company("0", "Google", "USA"));
        initial.add(new Company("1", "Apple", "USA"));
        viewModel.getMutableCompanies().setValue(initial);

        Company toDelete = initial.get(1);

        Call<Void> call = mock(Call.class);
        when(controller.deleteCompany("1")).thenReturn(call);

        ArgumentCaptor<Callback<Void>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.deleteCompany(toDelete);

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(null));

        List<Company> result = viewModel.getCompanies().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Google", result.get(0).getName());
    }




}
