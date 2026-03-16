package com.bivolaris.backend_capstone_project.mappers;


import com.bivolaris.backend_capstone_project.dtos.CreateDeviceRequest;
import com.bivolaris.backend_capstone_project.dtos.DeviceDto;
import com.bivolaris.backend_capstone_project.entities.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceDtoMapper {

    DeviceDto toDto(Device device);
    Device toEntity(DeviceDto deviceDto);

    @Mapping(target = "employeeId", ignore = true)
    Device toEntity(CreateDeviceRequest createDeviceRequest);
}
