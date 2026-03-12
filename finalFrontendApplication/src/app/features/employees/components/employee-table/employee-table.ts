import { Component, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { EmployeeService } from '../../services/employee-service';
import { LayoutService } from '../../../../core/services/layout.service';
import { Employee } from '../../entities/Employee';
import { MatDialog } from '@angular/material/dialog';
import { UpdateEmployeeDialog } from '../dialogs/update-employee-dialog/update-employee-dialog';
import { DeleteEmployeeDialog } from '../dialogs/delete-employee-dialog/delete-employee-dialog';
import { UnassignEmployeeDialog } from '../dialogs/unassign-employee-dialog/unassign-employee-dialog';
import { CompanyService } from '../../../companies/services/company-service';
import { AssignEmployeeDialog } from '../dialogs/assign-employee-dialog/assign-employee-dialog';

@Component({
  selector: 'app-employee-table',
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './employee-table.html',
  styleUrl: './employee-table.css',
})
export class EmployeeTable {


  layoutService = inject(LayoutService);
  employeeService = inject(EmployeeService);
  companyService = inject(CompanyService);
  dialog = inject(MatDialog);

  companyId = input<string>();

  data = this.employeeService.employees;
  loading = this.employeeService.loading;
  error = this.employeeService.error;


  columns = ["id", "name", "email", "actions"];





  updateEmployee(employee: Employee) {
    const dialogRef = this.dialog.open(UpdateEmployeeDialog, {
      width: '400px',
      data: employee
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const updatedEmployee: Employee = { 
          id: employee.id,
          name: result.name, 
          email: result.email,
          companyId: this.companyId() ?? employee.companyId // i take the companyId from the signal, if there is no company, take the company assign to employee
        };
        this.employeeService.updateEmployee(employee.id, updatedEmployee, this.companyId());
      }
    });
  }

  deleteEmployee(employee: Employee) {
    const dialogRef = this.dialog.open(DeleteEmployeeDialog, {
      width: '400px',
      data: employee
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.deleteEmployee(employee.id, this.companyId());
      }
    });
  }


  unassignEmployee(employee: Employee) {
    const companyName = this.companyService.companies().find(c => c.id === employee.companyId)?.name ?? 'this company';

    const dialogRef = this.dialog.open(UnassignEmployeeDialog, {
      width: '420px',
      data: {
        employeeName: employee.name,
        companyName,
      },
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) {
        return;
      }

      const updatedEmployee: Employee = {
        ...employee,
        companyId: '',
      };

      this.employeeService.updateEmployee(employee.id, updatedEmployee, this.companyId());
    });
  }

  assignEmployee(employee: Employee) {
    const dialogRef = this.dialog.open(AssignEmployeeDialog, {
      width: '420px',
      data: {
        employeeName: employee.name,
      },
    });

    dialogRef.afterClosed().subscribe((selectedCompanyId: string | null) => {
      if (!selectedCompanyId) {
        return;
      }

      const updatedEmployee: Employee = {
        ...employee,
        companyId: selectedCompanyId,
      };

      this.employeeService.updateEmployee(employee.id, updatedEmployee, this.companyId());
    });
  }

}
