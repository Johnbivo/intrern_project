import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { Employee } from '../../../../employees/entities/Employee';
import { EmployeeService } from '../../../../employees/services/employee-service';

interface AssignDialogData {
  companyId: string;
  deviceSerialNumber: string;
  deviceName: string;
}

@Component({
  selector: 'app-assign-device-dialog',
  imports: [
    FormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './assign-device-dialog.html',
  styleUrl: './assign-device-dialog.css'
})
export class AssignDeviceDialog implements OnInit {


  dialogRef = inject(MatDialogRef<AssignDeviceDialog>);
  data: AssignDialogData = inject(MAT_DIALOG_DATA);
  employeeService = inject(EmployeeService);

  employees = signal<Employee[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  selectedEmployeeId = '';

  ngOnInit(): void {
    if (!this.data.companyId) {
      this.error.set('No company selected for this device.');
      this.loading.set(false);
      return;
    }

    this.employeeService.loadEmployeesByCompanyId(this.data.companyId)
      .subscribe({
        next: (employees) => {
          this.employees.set(employees);
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Failed to load company employees.');
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
