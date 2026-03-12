import { Routes } from '@angular/router';
import { MenuCard } from './features/devices/components/menu-card/menu-card';
import { CompanyTable } from './features/companies/components/company-table/company-table';
import { DevicesPage } from './features/devices/pages/devices-page';
import { EmployeesPage } from './features/employees/pages/employee-page';
import { EmployeeMenuCard } from './features/employees/components/employee-menu-card/employee-menu-card';

export const routes: Routes = [
  { path: 'devices', component: MenuCard, data: { mode: 'devices' } },
  { path: 'devices/:companyId', component: DevicesPage },
  { path: 'employees', component: EmployeeMenuCard, data: { mode: 'employees' } },
  {path: 'employees/:companyId', component: EmployeesPage},
  { path: 'companies', component: CompanyTable },

  { path: '', redirectTo: 'companies', pathMatch: 'full' }
];
  