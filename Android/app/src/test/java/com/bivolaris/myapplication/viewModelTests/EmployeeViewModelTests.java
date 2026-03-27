package com.bivolaris.myapplication.viewModelTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bivolaris.myapplication.controller.EmployeeController;
import com.bivolaris.myapplication.model.CreateEmployeeRequest;
import com.bivolaris.myapplication.model.Employee;
import com.bivolaris.myapplication.viewModel.EmployeeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeViewModelTests {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    EmployeeController controller;

    private EmployeeViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new EmployeeViewModel(controller);
    }


    @Test
    public void testLoadEmployees() {

        List<Employee> employees = List.of(
                new Employee("0", "John", "Doe@example.com", "0"),
                new Employee("1", "Jane", "Smith@example.com", "0")
        );

        Call<List<Employee>> call = mock(Call.class);
        when(controller.getEmployees()).thenReturn(call);

        ArgumentCaptor<Callback<List<Employee>>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.loadEmployees();

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(employees));

        List<Employee> result = viewModel.getEmployees().getValue();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    public void testLoadEmployeesByCompany() {

        String companyId = "123";

        Call<List<Employee>> call = mock(Call.class);
        when(controller.getEmployeesByCompanyId(companyId)).thenReturn(call);

        ArgumentCaptor<Callback<List<Employee>>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.loadEmployeesByCompany(companyId);


        assertNull(viewModel.getEmployees().getValue());

        verify(call).enqueue(captor.capture());

        List<Employee> employees = List.of(
                new Employee("0", "John", "Doe@example.com", companyId)
        );

        captor.getValue().onResponse(call, Response.success(employees));

        List<Employee> result = viewModel.getEmployees().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(companyId, result.get(0).getCompanyId());
    }

    @Test
    public void testCreateEmployee() {

        CreateEmployeeRequest request =
                new CreateEmployeeRequest("John", "Doe@example.com", "123");

        // First call: createEmployee()
        Call<Void> createCall = mock(Call.class);
        when(controller.createEmployee(request)).thenReturn(createCall);

        // Second call: loadEmployees()
        Call<List<Employee>> getCall = mock(Call.class);
        when(controller.getEmployees()).thenReturn(getCall);

        ArgumentCaptor<Callback<Void>> createCaptor =
                ArgumentCaptor.forClass(Callback.class);

        ArgumentCaptor<Callback<List<Employee>>> getCaptor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.createEmployee(request);


        verify(createCall).enqueue(createCaptor.capture());
        createCaptor.getValue().onResponse(createCall, Response.success(null));


        verify(getCall).enqueue(getCaptor.capture());

        List<Employee> employees = List.of(
                new Employee("0", "John", "Doe@example.com", "123")
        );

        getCaptor.getValue().onResponse(getCall, Response.success(employees));

        List<Employee> result = viewModel.getEmployees().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    @Test
    public void testUpdateEmployee() {

        List<Employee> initial = new ArrayList<>();
        initial.add(new Employee("0", "John", "Doe@example.com", "123"));
        initial.add(new Employee("1", "Jane", "Smith@example.com", "123"));
        viewModel.getMutableEmployees().setValue(initial);

        Employee updated = new Employee("1", "Jane", "Johnson@example.com", "123");

        Call<Employee> call = mock(Call.class);
        when(controller.updateEmployee("1", updated)).thenReturn(call);

        ArgumentCaptor<Callback<Employee>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.updateEmployee(updated);

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(updated));

        List<Employee> result = viewModel.getEmployees().getValue();
        assertNotNull(result);
        assertEquals("Jane", result.get(1).getName());
    }

    @Test
    public void testDeleteEmployee() {

        List<Employee> initial = new ArrayList<>();
        initial.add(new Employee("0", "John", "doe@example.com", "123"));
        initial.add(new Employee("1", "Jane", "smith@example.com", "123"));
        viewModel.getMutableEmployees().setValue(initial);

        Call<Void> call = mock(Call.class);
        when(controller.deleteEmployee("1")).thenReturn(call);

        ArgumentCaptor<Callback<Void>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.deleteEmployee("1");

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(null));

        List<Employee> result = viewModel.getEmployees().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("0", result.get(0).getId());
    }

}
