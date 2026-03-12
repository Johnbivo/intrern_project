import { Component, OnInit, inject, signal, computed } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { EmployeeTable } from "../components/employee-table/employee-table";
import { EmployeeService } from "../services/employee-service";
import { CompanyService } from "../../companies/services/company-service";
import { MatDialog } from "@angular/material/dialog";
import { Employee } from "../entities/Employee";
import { CreateEmployeeDialog } from "../components/dialogs/create-employee-dialog/create-employee-dialog";




@Component({
    selector: 'app-employee-page',
    standalone: true,
    imports: [MatButtonModule, MatIconModule, EmployeeTable],
    template: `
    <div class="employees-page">
        
      <div class="header">
        <button mat-icon-button color="primary" (click)="goBack()">
          <mat-icon>arrow_back</mat-icon>
        </button>
        <h2>Employees for {{ companyName() }}</h2>
        <button mat-flat-button color="primary" (click)="addEmployee()">
          Add Employee
        </button>
      </div>
      <app-employee-table [companyId]="companyId()" />
    </div>
    `,
    styles: [`
    .employees-page {
      padding: 24px;
      min-height: 100vh;
      background: #f8fafc;
    }

    .header {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 32px;
      padding: 16px 0;
      border-bottom: 1px solid #e2e8f0;
    }

    h2 {
      margin: 0;
      color: #0f172a;
      font-size: 1.75rem;
      font-weight: 700;
      letter-spacing: -0.5px;
      flex: 1;
    }

    button[mat-icon-button] {
      color: #64748b;
      transition: color 0.2s ease;
    }

    button[mat-icon-button]:hover {
      color: #2563eb;
    }

`]
})
export class EmployeesPage implements OnInit {


    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private employeeService = inject(EmployeeService);
    private companyService = inject(CompanyService);
    dialog = inject(MatDialog);


    companyId = signal<string>('');
    companyName = computed(() => {
      if (this.companyId() === 'unemployed') {
        return 'Unemployed';
      }
        const companies = this.companyService.companies();
        const company = companies.find(c => c.id === this.companyId());
        return company ? company.name : 'Unknown Company';
    });

    ngOnInit() {
        this.route.paramMap.subscribe(params => {
            const id = params.get('companyId');
            if (id) {
                this.companyId.set(id);
          if (id === 'unemployed') {
            this.employeeService.loadUnemployedEmployees();
          } else {
            this.employeeService.loadEmployeesByCompanyId(id);
            if (this.companyService.companies().length === 0) {
              this.companyService.loadCompanies();
            }
          }
            }
        });
    }

    goBack() {
        this.router.navigate(['/employees']);
    }

    addEmployee() {
        const dialogRef = this.dialog.open(CreateEmployeeDialog, {
          width: '400px',
          data: { companyId: this.companyId() }
        });
        
        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            const newEmployee = {
              name: result.name,
              email: result.email,
              companyId: this.companyId() === 'unemployed' ? '' : this.companyId()
            };
            this.employeeService.createEmployee(newEmployee as Employee);
          }

    }
        );
      }
}