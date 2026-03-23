package com.bivolaris.backend_capstone_project.services.implementations;


import com.bivolaris.backend_capstone_project.dtos.CreateDeviceRequest;
import com.bivolaris.backend_capstone_project.dtos.DeviceDto;
import com.bivolaris.backend_capstone_project.entities.Device;
import com.bivolaris.backend_capstone_project.mappers.DeviceDtoMapper;
import com.bivolaris.backend_capstone_project.repositories.DeviceRepository;
import com.bivolaris.backend_capstone_project.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;


@Primary
@Service
@RequiredArgsConstructor
public class DeviceServiceImp implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceDtoMapper deviceDtoMapper;

    @Override
    public List<DeviceDto> getAllDevices(){
        return deviceRepository.findAll()
                .stream()
                .map(deviceDtoMapper::toDto)
                .toList();
    }

    @Override
    public DeviceDto getDeviceById(String id){
        return deviceRepository.findById(id).map(deviceDtoMapper::toDto).orElse(null);
    }

    @Override
    public boolean createDevice(CreateDeviceRequest createDeviceRequest){
        Device device = deviceDtoMapper.toEntity(createDeviceRequest);
        if (createDeviceRequest.getSerialNumber() == null || createDeviceRequest.getSerialNumber().isEmpty()){
            String serialNumber = generateSerialNumber();
            device.setSerialNumber(serialNumber);
        }else{
            device.setSerialNumber(createDeviceRequest.getSerialNumber());
        }

        var companyId = createDeviceRequest.getCompanyId();
        if(companyId != null){
            device.setCompanyId(companyId);
        }

        deviceRepository.save(device);
        return true;
    }

    @Override
    public DeviceDto updateDevice(String serialNumber, DeviceDto deviceDto){
        if(deviceRepository.findById(serialNumber).isEmpty()){
            return null;
        }
        Device device = deviceDtoMapper.toEntity(deviceDto);
        device.setSerialNumber(serialNumber);
        deviceRepository.save(device);
        return deviceDtoMapper.toDto(device);
    }

    @Override
    public boolean deleteDevice(String id){
        if (deviceRepository.findById(id).isEmpty()){
            return false;
        }
        deviceRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean assignDeviceToEmployee(String serialNumber, String employeeId){
        Device device = deviceRepository.findById(serialNumber).orElse(null);
        if (device == null){
            return false;
        }
        if (device.getEmployeeId() == null){
            device.setEmployeeId(employeeId);
            deviceRepository.save(device);
            return true;
        } else{
            return false; // already assigned to another employee
        }

    }

    @Override
    public List<DeviceDto> getAllDevicesAssignedToEmployee(String id){
        List<Device> devices = deviceRepository.findAllByEmployeeId(id);
        if (devices.isEmpty()){
            return null;
        }
        return devices.stream()
                .map(deviceDtoMapper::toDto)
                .toList();
    }

    public boolean unassignDeviceFromEmployee(String serialNumber, String employeeId){
        Device device = deviceRepository.findById(serialNumber).orElse(null);
        if(device == null){
            return false;
        }
        device.setEmployeeId(null);
        deviceRepository.save(device);
        return true;
    }



    @Override
    public void unassignAllDevicesFromEmployee(String employeeId){
        List<Device> devices = deviceRepository.findAllByEmployeeId(employeeId);
        for (Device device : devices){
            device.setEmployeeId(null);
            deviceRepository.save(device);
        }
    }

    @Override
    public void unassignAllDevicesFromCompany(String companyId){
        List<Device> devices = deviceRepository.findAllByCompanyId(companyId);
        for (Device device : devices){
            device.setCompanyId(null);
            deviceRepository.save(device);
        }
    }

    @Override
    public boolean assignDeviceToCompany(String serialNumber, String companyId) {
        Device device = deviceRepository.findById(serialNumber).orElse(null);
        if (device == null){
            return false;
        }
        device.setCompanyId(companyId);
        deviceRepository.save(device);
        return true;
    }

    @Override
    public boolean unassignDeviceFromCompany(String serialNumber, String companyId){
        Device device = deviceRepository.findById(serialNumber).orElse(null);
        if (device == null){
            return false;
        }
        device.setCompanyId(null);
        deviceRepository.save(device);
        return true;
    }

    @Override
    public List<DeviceDto> getAllDevicesAssignedToCompany(String companyId){
        return deviceRepository.findAllByCompanyId(companyId)
                .stream()
                .map(deviceDtoMapper::toDto)
                .toList();

    }


    private String generateSerialNumber(){
        long count = deviceRepository.count();
        long next = count + 1;
        return "SN" + String.format("%03d", next);
    }

}
