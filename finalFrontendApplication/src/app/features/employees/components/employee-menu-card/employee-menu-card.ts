import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CompanyService } from '../../../companies/services/company-service';
import { Company } from '../../../companies/entities/Company';


@Component({
  selector: 'app-employee-menu-card',
  imports: [MatCardModule, MatGridListModule, MatButtonModule, MatIconModule, MatProgressSpinnerModule],
  templateUrl: './employee-menu-card.html',
  styleUrl: './employee-menu-card.css',
})
export class EmployeeMenuCard implements OnInit{

  companyService = inject(CompanyService);
  private router = inject(Router);

  companies = this.companyService.companies;
  loading = this.companyService.loading;
  error = this.companyService.error;

  ngOnInit() {
    this.companyService.loadCompanies();
  }

  selectCompany(company: Company) {
    this.router.navigate([`/employees/${company.id}`]);
  }

  selectUnemployed() {
    this.router.navigate(['/employees/unemployed']);
  }
}
