import { Injectable } from "@angular/core";
import { Employee } from "../entities/Employee";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";




@Injectable({ providedIn: "root" })
export class EmployeeController {

    private url: string = "http://localhost:8080/employees";

     constructor(private http: HttpClient) {}

    getAllEmployees(): Observable<Employee[]> {
        return this.http.get<Employee[]>(this.url);
    }
    

    getAllEmployeesByCompanyId(companyId: string): Observable<Employee[]> {
        return this.http.get<Employee[]>(`${this.url}/company/${companyId}`);
    }

    getEmployeeById(id: string): Observable<Employee> {
        return this.http.get<Employee>(`${this.url}/${id}`);
    }

    createEmployee(employee: Employee): Observable<Employee> {
        return this.http.post<Employee>(this.url, employee);
    }

    updateEmployee(id: string, employee: Employee): Observable<Employee> {
        return this.http.put<Employee>(`${this.url}/${id}`, employee);
    }

    deleteEmployee(id: string): Observable<void> {
        return this.http.delete<void>(`${this.url}/${id}`);
    }



}