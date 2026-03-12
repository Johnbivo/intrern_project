package com.bivolaris.backend_capstone_project.controllers;


import com.bivolaris.backend_capstone_project.dtos.CreateDeviceRequest;
import com.bivolaris.backend_capstone_project.dtos.DeviceDto;
import com.bivolaris.backend_capstone_project.services.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
@Tag(name = "Device", description = "The Device API")
public class DeviceController {


    private final DeviceService deviceService;

    @GetMapping()
    @Operation(summary = "Get all devices", description = "Retrieves a list of all devices")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @ApiResponse(responseCode = "404", description = "No devices found")
    public ResponseEntity<List<DeviceDto>> getDevices(){
        if(deviceService.getAllDevices() == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{serialNumber}")
    @Operation(summary = "Get device by serial number", description = "Retrieves a single device by its serial number")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved device")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable String serialNumber)
            {
        if(deviceService.getDeviceById(serialNumber) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getDeviceById(serialNumber));
    }

    @PostMapping()
    @Operation(summary = "Create a new device", description = "Creates a new device based on the provided request body")
    @ApiResponse(responseCode = "201", description = "Successfully created device")
    @ApiResponse(responseCode = "400", description = "Invalid input or device already exists")
    public ResponseEntity<Void> createDevice(@RequestBody @Valid CreateDeviceRequest createDeviceRequest){
        if(!deviceService.createDevice(createDeviceRequest)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{serialNumber}")
    @Operation(summary = "Update an existing device", description = "Updates a device's details by its serial number")
    @ApiResponse(responseCode = "200", description = "Successfully updated device")
    @ApiResponse(responseCode = "400", description = "Invalid input or update failed")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable String serialNumber, @RequestBody @Valid DeviceDto deviceDto){
        DeviceDto updatedDevice = deviceService.updateDevice(serialNumber, deviceDto);
        if(updatedDevice == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/{serialNumber}")
    @Operation(summary = "Delete a device", description = "Deletes a device by its serial number")
    @ApiResponse(responseCode = "204", description = "Successfully deleted device")
    @ApiResponse(responseCode = "400", description = "Delete failed")
    public ResponseEntity<Void> deleteDevice(@PathVariable String serialNumber){
        if(!deviceService.deleteDevice(serialNumber)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/assign/{serialNumber}/employee/{employeeId}")
    @Operation(summary = "Assign device to employee", description = "Assigns a device to a specific employee")
    @ApiResponse(responseCode = "200", description = "Successfully assigned device")
    @ApiResponse(responseCode = "400", description = "Assignment failed")
    public ResponseEntity<Void> assignDeviceToEmployee(@PathVariable String serialNumber, @PathVariable String employeeId){
        if(!deviceService.assignDeviceToEmployee(serialNumber, employeeId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign/{serialNumber}/company/{companyId}")
    @Operation(summary = "Assign device to company", description = "Assigns a device to a specific company")
    @ApiResponse(responseCode = "200", description = "Successfully assigned device")
    @ApiResponse(responseCode = "400", description = "Assignment failed")
    public ResponseEntity<Void> assignDeviceToCompany(@PathVariable String serialNumber, @PathVariable String companyId){
        if(!deviceService.assignDeviceToCompany(serialNumber, companyId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }




    @PostMapping("/unassign/{serialNumber}/employee/{employeeId}")
    @Operation(summary = "Unassign device from employee", description = "Removes the employee assignment from a specific device")
    @ApiResponse(responseCode = "200", description = "Successfully unassigned device")
    @ApiResponse(responseCode = "400", description = "Unassignment failed")
    public ResponseEntity<Void> unassignDeviceFromEmployee(@PathVariable String serialNumber, @PathVariable String employeeId){
        if(!deviceService.unassignDeviceFromEmployee(serialNumber, employeeId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unassign/{serialNumber}/company/{companyId}")
    @Operation(summary = "Unassign device from company", description = "Removes the company assignment from a specific device")
    @ApiResponse(responseCode = "200", description = "Successfully unassigned device")
    @ApiResponse(responseCode = "400", description = "Unassignment failed")
    public ResponseEntity<Void> unassignDeviceFromCompany(@PathVariable String serialNumber, @PathVariable String companyId){
        if(!deviceService.unassignDeviceFromCompany(serialNumber, companyId)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all devices assigned to employee", description = "Retrieves a list of devices assigned to a specific employee")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @ApiResponse(responseCode = "404", description = "No devices found for this employee")
    public ResponseEntity<List<DeviceDto>> getAllDevicesAssignedToEmployee(@PathVariable String employeeId){
        if(deviceService.getAllDevicesAssignedToEmployee(employeeId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getAllDevicesAssignedToEmployee(employeeId));
    }

    @GetMapping("/company/{companyId}")
    @Operation(summary = "Get all devices assigned to company", description = "Retrieves a list of devices assigned to a specific company")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @ApiResponse(responseCode = "404", description = "No devices found for this company")
    public ResponseEntity<List<DeviceDto>> getAllDevicesAssignedToCompany(@PathVariable String companyId){
        if(deviceService.getAllDevicesAssignedToCompany(companyId) == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceService.getAllDevicesAssignedToCompany(companyId));
    }



    @PostMapping("/company/{companyId}/unassignAllDevices")
    @Operation(summary = "Unassign all devices from company", description = "Removes the company assignment from all devices belonging to the specified company")
    @ApiResponse(responseCode = "200", description = "Successfully unassigned all devices")
    public ResponseEntity<Void> unassignAllDevicesFromCompany(@PathVariable String companyId){
        deviceService.unassignAllDevicesFromCompany(companyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employee/{employeeId}/unassignAllDevices")
    @Operation(summary = "Unassign all devices from employee", description = "Removes the employee assignment from all devices belonging to the specified employee")
    @ApiResponse(responseCode = "200", description = "Successfully unassigned all devices")
    public ResponseEntity<Void> unassignAllDevicesFromEmployee(@PathVariable String employeeId){
        deviceService.unassignAllDevicesFromEmployee(employeeId);
        return ResponseEntity.ok().build();
    }




}
