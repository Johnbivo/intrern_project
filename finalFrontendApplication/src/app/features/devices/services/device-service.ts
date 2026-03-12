
import { inject, Injectable,signal } from '@angular/core';
import { Device } from '../entities/Device';
import { DeviceController } from '../controllers/device-controller';


@Injectable({ providedIn: 'root' })
export class DeviceService {


    deviceSignal = signal<Device | null>(null);
    devices = signal<Device[]>([]);
    device = signal<Device | null>(null);
    loading = signal(false)
    error = signal<string | null>(null);
    deviceController = inject(DeviceController);



    loadDevices(companyId?: string) {
        this.loading.set(true);
        this.error.set(null);
        if (companyId) {
            this.deviceController.getAllCompanyDevices(companyId).subscribe({
                next: (devices) => {
                    this.devices.set(devices);
                    this.loading.set(false);
                },
                error: (err) => {
                    this.error.set("Failed to load devices");
                    this.loading.set(false);
                }
            });
        } else {
            this.deviceController.getDevices().subscribe({
                next: (devices) => {
                    this.devices.set(devices);
                    this.loading.set(false);
                },
                error: (err) => {
                    this.error.set("Failed to load devices");
                    this.loading.set(false);
                }
            });
        }
    }

    loadDeviceBySerialNumber(serialNumber: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.getDeviceBySerialNumber(serialNumber).subscribe({
            next: (device) => {
                this.deviceSignal.set(device);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load device");
                this.loading.set(false);
            }
        });
    }

    createDevice(device: Device, companyId?: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.createDevice(device).subscribe({
            next: (newDevice) => {
                if (companyId) {
                    this.loadDevices(companyId);
                } else {
                    this.devices.update(list => [...list, newDevice]);
                    this.loading.set(false);
                }
            },
            error: (err) => {
                this.error.set("Failed to create device");
                this.loading.set(false);
            }
        });
    }


    updateDevice(serialNumber: string, device: Device, companyId?: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.updateDevice(serialNumber, device).subscribe({
            next: (updatedDevice) => {
                    this.devices.update(list =>
                        list.map(d => d.serialNumber === serialNumber ? updatedDevice : d)
                    );
                    this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to update device");
                this.loading.set(false);
            }
        });
    }

    deleteDevice(serialNumber: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.deleteDevice(serialNumber).subscribe({
            next: () => {
                this.devices.update(list =>
                    list.filter(d => d.serialNumber !== serialNumber)
                );
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to delete device");
                this.loading.set(false);
            }
        });
    }

    getAllCompanyDevices(companyId: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.getAllCompanyDevices(companyId).subscribe({
            next: (devices) => {
                this.devices.set(devices);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load company devices");
                this.loading.set(false);
            }
        });
    }

    getAllEmployeeDevices(employeeId: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.getAllEmployeeDevices(employeeId).subscribe({
            next: (devices) => {
                this.devices.set(devices);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load employee devices");
                this.loading.set(false);
            }
        });
    }

    assignDeviceToCompany(serialNumber: string, companyId: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.assignDeviceToCompany(serialNumber, companyId).subscribe({
            next: (updatedDevice) => {
                this.devices.update(list =>
                    list.map(d => d.serialNumber === serialNumber ? updatedDevice : d)
                );
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to assign device to company");
                this.loading.set(false);
            }
        });
    }

    assignDeviceToEmployee(serialNumber: string, employeeId: string, companyId?: string) {
        this.error.set(null);
        this.deviceController.assignDeviceToEmployee(serialNumber, employeeId).subscribe({
            next: () => {
                this.devices.update(list =>
                    list.map(d =>
                        d.serialNumber === serialNumber
                            ? { ...d, employeeId }
                            : d
                    )
                );
            },
            error: (err) => {
                const errorMsg = err?.error?.message || err?.message || "Failed to assign device to employee";
                this.error.set(`Assign failed: ${errorMsg} (HTTP ${err?.status || 'unknown'})`);
            }
        });
    }

    unassignDeviceFromEmployee(serialNumber: string, employeeId: string, companyId?: string) {
        this.error.set(null);
        this.deviceController.unassignDeviceFromEmployee(serialNumber, employeeId).subscribe({
            next: () => {
                this.devices.update(list =>
                    list.map(d =>
                        d.serialNumber === serialNumber
                            ? { ...d, employeeId: null }
                            : d
                    )
                );
            },
            error: (err) => {
                this.error.set("Failed to unassign device from employee");
            }
        });
    }

    unassignDeviceFromCompany(serialNumber: string, companyId: string) {
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.unassignDeviceFromCompany(serialNumber, companyId).subscribe({
            next: (updatedDevice) => {
                this.devices.update(list =>
                    list.map(d => d.serialNumber === serialNumber ? updatedDevice : d)
                );
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to unassign device from company");
                this.loading.set(false);
            }
        });
    }


    getAllDevicesAssignedToEmployee(employeeId: string){
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.getAllDevicesAssignedToEmployee(employeeId).subscribe({
            next: (devices) => {
                this.devices.set(devices);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load devices assigned to employee");
                this.loading.set(false);
            }
        });
    }


    getAllDevicesAssignedToCompany(companyId: string){
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.getAllDevicesAssignedToCompany(companyId).subscribe({
            next: (devices) => {
                this.devices.set(devices);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load devices assigned to company");
                this.loading.set(false);
            }
        });
    }

    unassignAllDevicesFromEmployee(employeeId: string){
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.unassignAllDevicesFromEmployee(employeeId).subscribe({
            next: () => {
                this.devices.update(list =>
                    list.filter(d => d.employeeId !== employeeId)
                );
                this.loading.set(false);
            },
            error: () => {
                this.error.set("Failed to unassign devices from employee");
                this.loading.set(false);
            }
        });
                
    }
    unassignAllDevicesFromCompany(companyId: string){
        this.loading.set(true);
        this.error.set(null);
        this.deviceController.unassignAllDevicesFromCompany(companyId).subscribe({
            next: () => {
                this.devices.update(list =>
                    list.filter(d => d.companyId !== companyId)
                );
                this.loading.set(false);
            },
            error: () => {
                this.error.set("Failed to unassign devices from company");
                this.loading.set(false);
            }
        });
    }





}







