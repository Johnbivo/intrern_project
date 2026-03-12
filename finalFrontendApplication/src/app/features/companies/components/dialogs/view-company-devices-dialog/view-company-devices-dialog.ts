import { Component,inject,OnInit, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DeviceController } from '../../../../devices/controllers/device-controller';
import { EmployeeController } from '../../../../employees/controllers/employee-controller';
import { Device } from '../../../../devices/entities/Device';


@Component({
  selector: 'app-view-company-devices-dialog',
  imports: [MatDialogModule, MatButtonModule, MatTableModule, MatProgressSpinnerModule],
  templateUrl: './view-company-devices-dialog.html',
  styleUrl: './view-company-devices-dialog.css',
})
export class ViewCompanyDevicesDialog {

  dialogRef = inject(MatDialogRef<ViewCompanyDevicesDialog>);
  data: { id: string; name: string } = inject(MAT_DIALOG_DATA);
  deviceController = inject(DeviceController);

  devices = signal<Device[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  columns = ['serialNumber', 'name', 'type'];



  ngOnInit(): void {
    this.deviceController.getAllCompanyDevices(this.data.id).subscribe({
      next: (list) => {
        this.devices.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load devices');
        this.loading.set(false);
      }
    });

  }

  close(): void {
    this.dialogRef.close();
  }

  
}
