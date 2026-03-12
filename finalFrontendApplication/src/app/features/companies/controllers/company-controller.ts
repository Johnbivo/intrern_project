import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Company } from "../entities/Company";
import { HttpClient } from "@angular/common/http";




@Injectable({ providedIn: "root" })
export class CompanyController {

    private url = "http://localhost:8080/companies";


    constructor(private http: HttpClient) {}


    // get all companies
    getCompanies(): Observable<Company[]> {
        return this.http.get<Company[]>(this.url);
    }

    // get company by id
    getCompanyById(id: string): Observable<Company> {
        return this.http.get<Company>(`${this.url}/${id}`);
    }

    // create company
    createCompany(company: Company): Observable<Company> {
        return this.http.post<Company>(this.url, company);
    }

    // update company
    updateCompany(id: string, company: Company): Observable<Company> {
        return this.http.put<Company>(`${this.url}/${id}`, company);
    }


    // delete company
    deleteCompany(id: string): Observable<void> {
        return this.http.delete<void>(`${this.url}/${id}`);
    }





}