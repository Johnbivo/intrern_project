import { Component, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DeviceService } from '../../services/device-service';
import { LayoutService } from '../../../../core/services/layout.service';
import { Device } from '../../entities/Device';
import { MatDialog } from '@angular/material/dialog';
import { UpdateDeviceDialog } from '../dialogs/update-device-dialog/update-device-dialog';
import { AssignDeviceDialog } from '../dialogs/assign-device-dialog/assign-device-dialog';
import { UnassignDeviceDialog } from '../dialogs/unassign-device-dialog/unassign-device-dialog';
import { DeleteDeviceDialog } from '../dialogs/delete-device-dialog/delete-device-dialog';


@Component({
  selector: 'app-device-table',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './device-table.html',
  styleUrl: './device-table.css',
})
export class DeviceTable {

  layoutService = inject(LayoutService);
  deviceService = inject(DeviceService);
  dialog = inject(MatDialog);

  companyId = input<string>();

  data = this.deviceService.devices;
  loading = this.deviceService.loading;
  error = this.deviceService.error;

  columns = ["serialNumber", "name", "type", "actions"];

  updateDevice(device: Device) {
    const dialogRef = this.dialog.open(UpdateDeviceDialog, {
      width: '400px',
      data: device
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const updatedDevice: Device = { 
          ...device,
          serialNumber: device.serialNumber,
          name: result.name, 
          type: result.type,
          companyId: this.companyId() ?? device.companyId,
          employeeId: device.employeeId
        };
        this.deviceService.updateDevice(device.serialNumber, updatedDevice, this.companyId());
      }
    });
  }


  deleteDevice(device: Device) {
    const dialogRef = this.dialog.open(DeleteDeviceDialog, {
      width: '400px',
      data: device
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deviceService.deleteDevice(device.serialNumber);
      }
    });
  }


  assignDevice(device: Device) {
    const selectedCompanyId = this.companyId();
    if (!selectedCompanyId) {
      return;
    }

    const dialogRef = this.dialog.open(AssignDeviceDialog, {
      width: '460px',
      data: {
        companyId: selectedCompanyId,
        deviceSerialNumber: device.serialNumber,
        deviceName: device.name
      }
    });

    dialogRef.afterClosed().subscribe((employeeId: string | null) => {
      if (employeeId) {
        this.deviceService.assignDeviceToEmployee(device.serialNumber, employeeId, selectedCompanyId);
      }
    });
  }



  unassignDevice(device: Device) {
    const selectedCompanyId = this.companyId();
    if (!selectedCompanyId) {
      return;
    }

    const dialogRef = this.dialog.open(UnassignDeviceDialog, {
      width: '460px',
      data: {
        companyId: selectedCompanyId,
        deviceSerialNumber: device.serialNumber,
        deviceName: device.name,
        currentEmployeeId: device.employeeId
      }
    });

    dialogRef.afterClosed().subscribe((employeeId: string | null) => {
      if (employeeId) {
        this.deviceService.unassignDeviceFromEmployee(device.serialNumber, employeeId, selectedCompanyId);
      }
    });
  }
}
