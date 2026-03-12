import { Component, inject, OnInit, signal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeeController } from '../../../../employees/controllers/employee-controller';
import { Employee } from '../../../../employees/entities/Employee';

@Component({
  selector: 'app-view-company-employees-dialog',
  imports: [MatDialogModule, MatButtonModule, MatTableModule, MatProgressSpinnerModule],
  templateUrl: './view-company-employees-dialog.html',
  styleUrl: './view-company-employees-dialog.css',
})
export class ViewCompanyEmployeesDialog implements OnInit {
  dialogRef = inject(MatDialogRef<ViewCompanyEmployeesDialog>);
  data: { id: string; name: string } = inject(MAT_DIALOG_DATA);
  employeeController = inject(EmployeeController);

  employees = signal<Employee[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  columns = ['name', 'email'];



  ngOnInit(): void {
    this.employeeController.getAllEmployeesByCompanyId(this.data.id).subscribe({
      next: (list) => {
        this.employees.set(list);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 404) {
          this.employees.set([]);
        } else {
          this.error.set('Failed to load employees');
        }
        this.loading.set(false);
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }
}
