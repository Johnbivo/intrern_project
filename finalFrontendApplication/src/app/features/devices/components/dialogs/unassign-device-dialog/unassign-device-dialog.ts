import { Component, inject, OnInit, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { EmployeeService } from '../../../../employees/services/employee-service';

interface UnassignDialogData {
  companyId: string;
  deviceSerialNumber: string;
  deviceName: string;
  currentEmployeeId: string | null;
}

@Component({
  selector: 'app-unassign-device-dialog',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButtonModule
  ],
  templateUrl: './unassign-device-dialog.html',
  styleUrl: './unassign-device-dialog.css'
})
export class UnassignDeviceDialog implements OnInit {
  dialogRef = inject(MatDialogRef<UnassignDeviceDialog>);
  data: UnassignDialogData = inject(MAT_DIALOG_DATA);
  employeeService = inject(EmployeeService);

  loading = signal(false);
  error = signal<string | null>(null);
  employeeName = signal<string>('');

  selectedEmployeeId = '';

  ngOnInit(): void {
    if (!this.data.currentEmployeeId) {
      this.error.set('This device is not assigned to any employee.');
      return;
    }

    this.selectedEmployeeId = this.data.currentEmployeeId;
    this.loading.set(true);
    this.employeeService.getEmployeeById(this.selectedEmployeeId).subscribe({
      next: (employee) => {
        this.employeeName.set(employee.name);
        this.loading.set(false);
      },
      error: () => {
        this.employeeName.set('Unknown employee');
        this.loading.set(false);
      }
    });
  }

  confirm(): void {
    if (!this.selectedEmployeeId) {
      return;
    }
    this.dialogRef.close(this.selectedEmployeeId);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
