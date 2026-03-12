package com.bivolaris.backend_capstone_project.services;


import com.bivolaris.backend_capstone_project.dtos.CreateDeviceRequest;
import com.bivolaris.backend_capstone_project.dtos.DeviceDto;

import java.util.List;

public interface DeviceService {

    List<DeviceDto> getAllDevices();
    DeviceDto getDeviceById(String id);
    boolean createDevice(CreateDeviceRequest createDeviceRequest);
    DeviceDto updateDevice(String id, DeviceDto deviceDto);
    boolean deleteDevice(String id);


    boolean assignDeviceToEmployee(String serialNumber, String employeeId);
    boolean assignDeviceToCompany(String serialNumber, String companyId);
    List<DeviceDto> getAllDevicesAssignedToEmployee(String employeeId);
    List<DeviceDto> getAllDevicesAssignedToCompany(String companyId);
    boolean unassignDeviceFromEmployee(String serialNumber, String employeeId);
    boolean unassignDeviceFromCompany(String serialNumber, String companyId);

    void unassignAllDevicesFromEmployee(String employeeId);
    void unassignAllDevicesFromCompany(String companyId);
}
