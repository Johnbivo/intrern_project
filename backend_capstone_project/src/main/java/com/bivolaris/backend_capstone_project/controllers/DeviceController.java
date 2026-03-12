package com.bivolaris.backend_capstone_project.controllers;


import com.bivolaris.backend_capstone_project.dtos.CreateDeviceRequest;
import com.bivolaris.backend_capstone_project.dtos.DeviceDto;
import com.bivolaris.backend_capstone_project.services.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {


    private final DeviceService deviceService;

    @GetMapping()
    public ResponseEntity<List<DeviceDto>> getDevices(){
        if(deviceService.getAllDevices() == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable String serialNumber)
            {
        if(deviceService.getDeviceById(serialNumber) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getDeviceById(serialNumber));
    }

    @PostMapping()
    public ResponseEntity<Void> createDevice(@RequestBody @Valid CreateDeviceRequest createDeviceRequest){
        if(!deviceService.createDevice(createDeviceRequest)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{serialNumber}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable String serialNumber, @RequestBody @Valid DeviceDto deviceDto){
        DeviceDto updatedDevice = deviceService.updateDevice(serialNumber, deviceDto);
        if(updatedDevice == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String serialNumber){
        if(!deviceService.deleteDevice(serialNumber)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/assign/{serialNumber}/employee/{employeeId}")
    public ResponseEntity<Void> assignDeviceToEmployee(@PathVariable String serialNumber, @PathVariable String employeeId){
        if(!deviceService.assignDeviceToEmployee(serialNumber, employeeId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign/{serialNumber}/company/{companyId}")
    public ResponseEntity<Void> assignDeviceToCompany(@PathVariable String serialNumber, @PathVariable String companyId){
        if(!deviceService.assignDeviceToCompany(serialNumber, companyId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }




    @PostMapping("/unassign/{serialNumber}/employee/{employeeId}")
    public ResponseEntity<Void> unassignDeviceFromEmployee(@PathVariable String serialNumber, @PathVariable String employeeId){
        if(!deviceService.unassignDeviceFromEmployee(serialNumber, employeeId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unassign/{serialNumber}/company/{companyId}")
    public ResponseEntity<Void> unassignDeviceFromCompany(@PathVariable String serialNumber, @PathVariable String companyId){
        if(!deviceService.unassignDeviceFromCompany(serialNumber, companyId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<DeviceDto>> getAllDevicesAssignedToEmployee(@PathVariable String employeeId){
        if(deviceService.getAllDevicesAssignedToEmployee(employeeId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getAllDevicesAssignedToEmployee(employeeId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DeviceDto>> getAllDevicesAssignedToCompany(@PathVariable String companyId){
        if(deviceService.getAllDevicesAssignedToCompany(companyId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getAllDevicesAssignedToCompany(companyId));
    }



    @PostMapping("/company/{companyId}/unassignAllDevices")
    public ResponseEntity<Void> unassignAllDevicesFromCompany(@PathVariable String companyId){
        deviceService.unassignAllDevicesFromCompany(companyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employee/{employeeId}/unassignAllDevices")
    public ResponseEntity<Void> unassignAllDevicesFromEmployee(@PathVariable String employeeId){
        deviceService.unassignAllDevicesFromEmployee(employeeId);
        return ResponseEntity.ok().build();
    }




}
