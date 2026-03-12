import { Component, inject, OnInit } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { CompanyService } from '../../../../companies/services/company-service';

interface AssignEmployeeDialogData {
  employeeName: string;
}

@Component({
  selector: 'app-assign-employee-dialog',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
  ],
  templateUrl: './assign-employee-dialog.html',
  styleUrl: './assign-employee-dialog.css',
})
export class AssignEmployeeDialog implements OnInit {
  dialogRef = inject(MatDialogRef<AssignEmployeeDialog>);
  data: AssignEmployeeDialogData = inject(MAT_DIALOG_DATA);
  companyService = inject(CompanyService);

  companies = this.companyService.companies;
  selectedCompanyId = '';

  ngOnInit(): void {
    if (this.companies().length === 0) {
      this.companyService.loadCompanies();
    }
  }

  confirm(): void {
    if (!this.selectedCompanyId) {
      return;
    }
    this.dialogRef.close(this.selectedCompanyId);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
