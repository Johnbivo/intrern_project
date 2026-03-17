import { inject, Injectable,signal } from "@angular/core";
import { Company } from "../entities/Company";
import { CompanyController } from "../controllers/company-controller";
import { tap } from "rxjs/internal/operators/tap";


@Injectable({ providedIn: "root" })
export class CompanyService {

    companySignal = signal<Company | null>(null);
    companies = signal<Company[]>([]);
    loading = signal(false);
    error = signal<string | null>(null);
    companyController = inject(CompanyController);
    //change names



    loadCompanies(){
        this.loading.set(true);
        this.error.set(null);
        this.companyController.getCompanies().subscribe({
            next: (companies) => {
                this.companies.set(companies);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load companies");
                this.loading.set(false);
            }
        });
    }


    loadCompanyById(id: string) {
        this.loading.set(true);
        this.error.set(null);
        this.companyController.getCompanyById(id).subscribe({
            next: (company) => {
                this.companySignal.set(company);
                this.loading.set(false);
            },
            error: (err) => {
                this.error.set("Failed to load company");
                this.loading.set(false);
            }
        });
    }

 
    updateCompany(id: string, company: Company) {
        this.error.set(null);
        this.companyController.updateCompany(id, company).subscribe({
            next: (updatedCompany) => {
                this.companies.update(list =>
                    list.map(c => c.id === id ? updatedCompany : c)
                );
            },
            error: (err) => {
                this.error.set("Failed to update company");
            }
        });
    }


    
    createCompany(company: Omit<Company, 'id'>) {
  this.error.set(null);

  this.companyController.createCompany(company as Company).subscribe({
    next: (newCompany) => {
      this.companies.update(list => [...list, newCompany]);
    },
    error: () => {
      this.error.set('Failed to create company');
    }
  });
}





    deleteCompany(id: string) {
        // Don't set loading during delete to avoid table blink
        this.error.set(null);
        this.companyController.deleteCompany(id).subscribe({
            next: () => {
                this.companies.update(list => list.filter(c => c.id !== id));
            },
            error: (err) => {
                this.error.set('Failed to delete company');
            }
        }); 
    }





}