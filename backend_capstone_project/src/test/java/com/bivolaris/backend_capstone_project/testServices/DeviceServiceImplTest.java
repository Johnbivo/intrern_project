package com.bivolaris.backend_capstone_project.testServices;


import com.bivolaris.backend_capstone_project.dtos.CreateDeviceRequest;
import com.bivolaris.backend_capstone_project.dtos.DeviceDto;
import com.bivolaris.backend_capstone_project.entities.Device;
import com.bivolaris.backend_capstone_project.mappers.DeviceDtoMapper;
import com.bivolaris.backend_capstone_project.repositories.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivolaris.backend_capstone_project.services.implementations.DeviceServiceImp;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceImplTest {


    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DeviceDtoMapper deviceDtoMapper;
    @InjectMocks
    private DeviceServiceImp deviceService;


    private Device testDevice;
    private DeviceDto testDeviceDto;
    private CreateDeviceRequest testCreateDeviceRequest;


    @BeforeEach
    void setUp() {

        testDevice = new Device();
        testDevice.setSerialNumber("SN001");
        testDevice.setName("Test Laptop");
        testDevice.setType("Laptop");



        testDeviceDto = new DeviceDto();
        testDeviceDto.setSerialNumber("SN001");
        testDeviceDto.setName("Test Laptop");
        testDeviceDto.setType("Laptop");

        testCreateDeviceRequest = new CreateDeviceRequest();
        testCreateDeviceRequest.setName("Test Laptop");
        testCreateDeviceRequest.setType("Laptop");
        testCreateDeviceRequest.setSerialNumber(null);



    }


    @Test
    @DisplayName("Should return a list of devices")
    void getAllDevices_ShouldReturnListOfDevices() {
        when(deviceRepository.findAll()).thenReturn(List.of(testDevice));
        when(deviceDtoMapper.toDto(testDevice)).thenReturn(testDeviceDto);

        List<DeviceDto> result = deviceService.getAllDevices();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDeviceDto, result.get(0));
        verify(deviceRepository).findAll();
        verify(deviceDtoMapper).toDto(testDevice);
    }


    @Test
    @DisplayName("Should return a device when a valid id is given")
    void getDeviceById_ValidId_ReturnsDevice() {

        String id = "SN001";
        when(deviceRepository.findById(id)).thenReturn(Optional.of(testDevice));
        when(deviceDtoMapper.toDto(testDevice)).thenReturn(testDeviceDto);

        DeviceDto result = deviceService.getDeviceById(id);

        assertNotNull(result);
        assertEquals("SN001", result.getSerialNumber()); // could put id too
        assertEquals("Test Laptop", result.getName());
        assertEquals("Laptop", result.getType());
        assertEquals(testDeviceDto, result);
    }





    @Test
    @DisplayName("Should generate serialNumber when not given")
    void createDevice_NoGivenSerialNumber_ShouldGeneratesSerialNumber() {


        when(deviceDtoMapper.toEntity(testCreateDeviceRequest)).thenReturn(testDevice);

        boolean result = deviceService.createDevice(testCreateDeviceRequest);

        assert result;
        assertNotNull(testDevice.getSerialNumber());
        assertEquals("SN001", testDevice.getSerialNumber());
    }

    @Test
    @DisplayName("Should update device with the given id and device dto")
    void updateDevice_ValidIdAndDeviceDto_ShouldUpdateDevice() {

        String id = "SN001";

        when(deviceRepository.findById(id)).thenReturn(Optional.of(testDevice));
        when(deviceDtoMapper.toEntity(testDeviceDto)).thenReturn(testDevice);
        when(deviceDtoMapper.toDto(testDevice)).thenReturn(testDeviceDto);

        DeviceDto result = deviceService.updateDevice(id, testDeviceDto);

        assertNotNull(result);
        assertEquals("SN001", result.getSerialNumber());
        assertEquals("Test Laptop", result.getName());
        assertEquals("Laptop", result.getType());
        assertEquals(testDeviceDto, result);

        verify(deviceRepository).save(testDevice);


    }

    @Test
    @DisplayName("Should delete device with the provided id")
    void deleteDevice_ValidId_ShouldDeleteDevice() {
        String id = "SN001";
        when(deviceRepository.findById(id)).thenReturn(Optional.of(testDevice));
        boolean result = deviceService.deleteDevice(id);
        assertTrue(result);
        verify(deviceRepository).deleteById(id);
    }


    @Test
    @DisplayName("Should assign the device with the given employeeID")
    void assignDeviceToEmployee_ValidEmployeeId_ShouldAssignDevice() {


        Device device = new Device();
         device.setSerialNumber("SN001");
         device.setEmployeeId(null);
         when(deviceRepository.findById("SN001")).thenReturn(Optional.of(device));

         
        boolean assigned = deviceService.assignDeviceToEmployee("SN001", "123");
        assertTrue(assigned);

        assertEquals("123", device.getEmployeeId());
        verify(deviceRepository).save(device);


    }

    @Test
    @DisplayName("Should return all devices assigned to an employee, with employeeID")
    void getAllDevicesAssignedToEmployee_ValidEmployeeId_ShouldReturnDevices() {
        String employeeId = "123";
        List<Device> devices = List.of(testDevice);


        when(deviceRepository.findAllByEmployeeId(employeeId)).thenReturn(devices);
        when(deviceDtoMapper.toDto(testDevice)).thenReturn(testDeviceDto);
        List<DeviceDto> result = deviceService.getAllDevicesAssignedToEmployee(employeeId);


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("SN001", result.get(0).getSerialNumber());
    }


    @Test
    @DisplayName("Should assign a device to a company")
    void assignDeviceToCompany_ValidCompanyId_ShouldAssignDevice() {
        Device device = new Device();
        device.setSerialNumber("SN001");
        device.setCompanyId(null);
        when(deviceRepository.findById("SN001")).thenReturn(Optional.of(device));

        boolean assigned = deviceService.assignDeviceToCompany("SN001", "123");
        assertTrue(assigned);
        assertEquals("123", device.getCompanyId());
        verify(deviceRepository).save(device);
    }










}
