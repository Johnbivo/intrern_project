import { Component, effect, inject, OnInit } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog } from '@angular/material/dialog';
import { LayoutService } from '../../../../core/services/layout.service';
import { CompanyService } from '../../services/company-service';
import { CreateCompanyDialog } from '../dialogs/create-company-dialog/create-company-dialog';
import { UpdateCompanyDialog } from '../dialogs/update-company-dialog/update-company-dialog';
import { Company } from '../../entities/Company';
import { DeleteCompanyDialog } from '../dialogs/delete-company-dialog/delete-company-dialog';
import { ViewCompanyEmployeesDialog } from '../dialogs/view-company-employees-dialog/view-company-employees-dialog';
import { ViewCompanyDevicesDialog } from '../dialogs/view-company-devices-dialog/view-company-devices-dialog';

@Component({
  selector: 'app-company-table',
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './company-table.html',
  styleUrl: './company-table.css',
})
export class CompanyTable implements OnInit{


  ngOnInit(): void {
    this.companyService.loadCompanies();
  }




  layoutService = inject(LayoutService);
  companyService = inject(CompanyService);
  dialog = inject(MatDialog);


  // switched to using MatTableDataSource to try to stop blinking on update, but it didn't work
  data = new MatTableDataSource<Company>([]);
  
  constructor() {
    effect(() => {
      this.data.data = this.companyService.companies();
    });
  }

  loading = this.companyService.loading;

  error = this.companyService.error;
  

  columns = ["id", "name", "address", "actions"];


  // optization for table (not working) to stop blinking
  trackByCompanyId(index: number, company: Company): string {
    return company.id ?? index;
  }

  createCompany() {
    const dialogRef = this.dialog.open(CreateCompanyDialog);

    dialogRef.afterClosed().subscribe(result => {
      if (!result) return;
      this.companyService.createCompany(result);
    });
  }

  
  updateCompany(company: Company) {
    const dialogRef = this.dialog.open(UpdateCompanyDialog, {
      data: company
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.companyService.updateCompany(company.id, { ...company, ...result });
       
      }
    });
  }

  viewEmployees(company: Company) {
    const dialogRef = this.dialog.open(ViewCompanyEmployeesDialog, {
      data: { id: company.id, name: company.name }
    });
    
    
  }

  viewDevices(company: Company) {
    const dialogRef = this.dialog.open(ViewCompanyDevicesDialog, {
      data: { id: company.id, name: company.name }
    });
  }


  deleteCompany(company: Company) {
    const dialogRef = this.dialog.open(DeleteCompanyDialog, {
      data: company
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.companyService.deleteCompany(company.id);
      }
    });
  }

}
