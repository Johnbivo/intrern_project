package com.bivolaris.myapplication.viewModelTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bivolaris.myapplication.controller.DeviceController;
import com.bivolaris.myapplication.model.CreateDeviceRequest;
import com.bivolaris.myapplication.model.Device;
import com.bivolaris.myapplication.viewModel.DeviceViewModel;

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
public class DeviceViewModelTests {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    DeviceController controller;

    private DeviceViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new DeviceViewModel(controller);
    }


    @Test
    public void testLoadDevices() {

        List<Device> devices = List.of(
                new Device("A1", "Laptop", "Dell", null, null),
                new Device("B2", "Phone", "Samsung", null, null)
        );

        Call<List<Device>> call = mock(Call.class);
        when(controller.getDevices()).thenReturn(call);

        ArgumentCaptor<Callback<List<Device>>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.loadDevices();

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(devices));

        List<Device> result = viewModel.getDevices().getValue();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("A1", result.get(0).getSerialNumber());
    }

    @Test
    public void testLoadDevicesByCompany() {

        String companyId = "123";

        Call<List<Device>> call = mock(Call.class);
        when(controller.getDevicesByCompanyId(companyId)).thenReturn(call);

        ArgumentCaptor<Callback<List<Device>>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.loadDevicesByCompany(companyId);

        verify(call).enqueue(captor.capture());

        List<Device> devices = List.of(
                new Device("A1", "Laptop", "Dell", companyId, null)
        );

        captor.getValue().onResponse(call, Response.success(devices));

        List<Device> result = viewModel.getDevices().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(companyId, result.get(0).getCompanyId());
    }

    @Test
    public void testCreateDevice() {

        CreateDeviceRequest request =
                new CreateDeviceRequest("A1", "Laptop", "Dell", null);

        Call<Void> createCall = mock(Call.class);
        when(controller.createDevice(request)).thenReturn(createCall);

        Call<List<Device>> getCall = mock(Call.class);
        when(controller.getDevices()).thenReturn(getCall);

        ArgumentCaptor<Callback<Void>> createCaptor =
                ArgumentCaptor.forClass(Callback.class);

        ArgumentCaptor<Callback<List<Device>>> getCaptor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.createDevice(request);

        verify(createCall).enqueue(createCaptor.capture());
        createCaptor.getValue().onResponse(createCall, Response.success(null));

        verify(getCall).enqueue(getCaptor.capture());

        List<Device> devices = List.of(
                new Device("A1", "Laptop", "Dell", null, null)
        );

        getCaptor.getValue().onResponse(getCall, Response.success(devices));

        List<Device> result = viewModel.getDevices().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getSerialNumber());
    }

    @Test
    public void testUpdateDevice() {

        List<Device> initial = new ArrayList<>();
        initial.add(new Device("A1", "Laptop", "Dell", null, null));
        initial.add(new Device("B2", "Phone", "Samsung", null, null));
        viewModel.getMutableDevices().setValue(initial);

        Device updated = new Device("B2", "Phone", "Google Pixel", null, null);

        Call<Device> call = mock(Call.class);
        when(controller.updateDevice("B2", updated)).thenReturn(call);

        ArgumentCaptor<Callback<Device>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.updateDevice(updated);

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(updated));

        List<Device> result = viewModel.getDevices().getValue();
        assertNotNull(result);
        assertEquals("Google Pixel", result.get(1).getType());
    }

    @Test
    public void testDeleteDevice() {

        List<Device> initial = new ArrayList<>();
        initial.add(new Device("A1", "Laptop", "Dell", null, null));
        initial.add(new Device("B2", "Phone", "Samsung", null, null));
        viewModel.getMutableDevices().setValue(initial);

        Call<Void> call = mock(Call.class);
        when(controller.deleteDevice("B2")).thenReturn(call);

        ArgumentCaptor<Callback<Void>> captor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.deleteDevice("B2");

        verify(call).enqueue(captor.capture());
        captor.getValue().onResponse(call, Response.success(null));

        List<Device> result = viewModel.getDevices().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getSerialNumber());
    }

    @Test
    public void testAssignDeviceToCompany() {

        Call<Void> assignCall = mock(Call.class);
        when(controller.assignDeviceToCompany("A1", "123")).thenReturn(assignCall);

        Call<List<Device>> getCall = mock(Call.class);
        when(controller.getDevices()).thenReturn(getCall);

        ArgumentCaptor<Callback<Void>> assignCaptor =
                ArgumentCaptor.forClass(Callback.class);

        ArgumentCaptor<Callback<List<Device>>> getCaptor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.assignDeviceToCompany("A1", "123");

        verify(assignCall).enqueue(assignCaptor.capture());
        assignCaptor.getValue().onResponse(assignCall, Response.success(null));

        verify(getCall).enqueue(getCaptor.capture());
    }

    @Test
    public void testUnassignDeviceFromCompany() {

        Call<Void> unassignCall = mock(Call.class);
        when(controller.unassignDeviceFromCompany("A1", "123")).thenReturn(unassignCall);

        Call<List<Device>> getCall = mock(Call.class);
        when(controller.getDevices()).thenReturn(getCall);

        ArgumentCaptor<Callback<Void>> unassignCaptor =
                ArgumentCaptor.forClass(Callback.class);

        ArgumentCaptor<Callback<List<Device>>> getCaptor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.unassignDeviceFromCompany("A1", "123");

        verify(unassignCall).enqueue(unassignCaptor.capture());
        unassignCaptor.getValue().onResponse(unassignCall, Response.success(null));

        verify(getCall).enqueue(getCaptor.capture());
    }

    @Test
    public void testAssignDeviceToEmployee() {

        Call<Void> assignCall = mock(Call.class);
        when(controller.assignDeviceToEmployee("A1", "E1")).thenReturn(assignCall);

        Call<List<Device>> getCall = mock(Call.class);
        when(controller.getDevices()).thenReturn(getCall);

        ArgumentCaptor<Callback<Void>> assignCaptor =
                ArgumentCaptor.forClass(Callback.class);

        ArgumentCaptor<Callback<List<Device>>> getCaptor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.assignDeviceToEmployee("A1", "E1");

        verify(assignCall).enqueue(assignCaptor.capture());
        assignCaptor.getValue().onResponse(assignCall, Response.success(null));

        verify(getCall).enqueue(getCaptor.capture());
    }

    @Test
    public void testUnassignDeviceFromEmployee() {

        Call<Void> unassignCall = mock(Call.class);
        when(controller.unassignDeviceFromEmployee("A1", "E1")).thenReturn(unassignCall);

        Call<List<Device>> getCall = mock(Call.class);
        when(controller.getDevices()).thenReturn(getCall);

        ArgumentCaptor<Callback<Void>> unassignCaptor =
                ArgumentCaptor.forClass(Callback.class);

        ArgumentCaptor<Callback<List<Device>>> getCaptor =
                ArgumentCaptor.forClass(Callback.class);

        viewModel.unassignDeviceFromEmployee("A1", "E1");

        verify(unassignCall).enqueue(unassignCaptor.capture());
        unassignCaptor.getValue().onResponse(unassignCall, Response.success(null));

        verify(getCall).enqueue(getCaptor.capture());
    }
}


