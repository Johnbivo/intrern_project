import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { DeviceController } from '../controllers/device-controller';
import { Device } from '../entities/Device';
import { DeviceService } from './device-service';




describe('DeviceService', () => {

    let service: DeviceService;

    const deviceFactory = (overrides: Partial<Device> = {}): Device => ({
        serialNumber: 'SN-001',
        name: 'Device 1',
        type: 'Laptop',
        companyId: null,
        employeeId: null,
        ...overrides,
    });


    const deviceControllerMock = {
        getDevices: vi.fn(),
        loadDeviceBySerialNumber: vi.fn(),
        getAllDevicesByCompanyId: vi.fn(),
        createDevice: vi.fn(),
        updateDevice: vi.fn(),
        deleteDevice: vi.fn(),
    };


    beforeEach(() => {
        vi.clearAllMocks();

        TestBed.configureTestingModule({
            providers: [
                DeviceService,
                { provide: DeviceController, useValue: deviceControllerMock },
            ],
        });

        service = TestBed.inject(DeviceService);
        service.deviceSignal.set(null);
        service.devices.set([]);
        service.loading.set(false);
        service.error.set(null);
    });


    it('loadDevices should set devices on success', () => {
        const devices = [deviceFactory()];
        deviceControllerMock.getDevices.mockReturnValue(of(devices));
        service.loadDevices();
        expect(deviceControllerMock.getDevices).toHaveBeenCalledTimes(1);
        expect(service.devices()).toEqual(devices);
        expect(service.loading()).toBe(false);
        expect(service.error()).toBeNull();
    });




    it('loadDevices should set error on failure', () => {
        const error = new Error('Failed to load devices');
        deviceControllerMock.getDevices.mockReturnValue(throwError(() => error));
        service.loadDevices();
        
        expect(deviceControllerMock.getDevices).toHaveBeenCalledTimes(1);
        expect(service.error()).toBe('Failed to load devices');
        expect(service.loading()).toBe(false);
    });



    it('should create', () => {
        expect(service).toBeTruthy();
    });



    it("createDevice should append new device on success", () => {
        const existing = deviceFactory({ serialNumber: 'dev-1' });
        const newDevice = deviceFactory({ serialNumber: 'dev-2' });
        service.devices.set([existing]);

        deviceControllerMock.createDevice.mockReturnValue(of(newDevice));
        service.createDevice(newDevice);


        expect(deviceControllerMock.createDevice).toHaveBeenCalledWith(newDevice);
        expect(service.devices()).toEqual([existing, newDevice]);
        expect(service.error()).toBeNull();
    });



    it("createDevice should set error on failure", () => {
        const newDevice = deviceFactory({ serialNumber: 'dev-2' });
        const error = new Error('Failed to create device');

        deviceControllerMock.createDevice.mockReturnValue(throwError(() => error));
        service.createDevice(newDevice);


        expect(deviceControllerMock.createDevice).toHaveBeenCalledWith(newDevice);
        expect(service.error()).toBe('Failed to create device');
    });



    it("updateDevice should update device on success", () => {
        const existing = deviceFactory({ serialNumber: 'dev-1', name: 'Old Name' });
        const updated = deviceFactory({ serialNumber: 'dev-1', name: 'New Name' });


        service.devices.set([existing]);
        deviceControllerMock.updateDevice.mockReturnValue(of(updated));
        service.updateDevice(existing.serialNumber, updated);


        expect(deviceControllerMock.updateDevice).toHaveBeenCalledWith(existing.serialNumber, updated);
        expect(service.devices()).toEqual([updated]);
        expect(service.error()).toBeNull();
    });


    it("updateDevice should set error on failure", () => {
        const existing = deviceFactory({ serialNumber: 'dev-1', name: 'Old Name' });
        const updated = deviceFactory({ serialNumber: 'dev-1', name: 'New Name' });
        const error = new Error('Failed to update device');

        service.devices.set([existing]);


        deviceControllerMock.updateDevice.mockReturnValue(throwError(() => error));
        service.updateDevice(existing.serialNumber, updated);


        expect(deviceControllerMock.updateDevice).toHaveBeenCalledWith(existing.serialNumber, updated);
        expect(service.error()).toBe('Failed to update device');
    });



    it("deleteDevice should remove device on success", () => {
        const existing = deviceFactory({ serialNumber: 'dev-1' });


        service.devices.set([existing]);
        deviceControllerMock.deleteDevice.mockReturnValue(of(void 0));
        service.deleteDevice(existing.serialNumber);


        expect(deviceControllerMock.deleteDevice).toHaveBeenCalledWith(existing.serialNumber);
        expect(service.devices()).toEqual([]);
        expect(service.error()).toBeNull();
    });

    it("deleteDevice should set error on failure", () => {

        const existing = deviceFactory({ serialNumber: 'dev-1' });
        const error = new Error('Failed to delete device');
        service.devices.set([existing]);

        deviceControllerMock.deleteDevice.mockReturnValue(throwError(() => error));
        service.deleteDevice(existing.serialNumber);


        expect(deviceControllerMock.deleteDevice).toHaveBeenCalledWith(existing.serialNumber);
        expect(service.error()).toBe('Failed to delete device');
    });

    


});