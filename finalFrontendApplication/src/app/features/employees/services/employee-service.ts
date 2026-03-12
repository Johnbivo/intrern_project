import { Injectable,signal, inject } from "@angular/core";
import { Employee } from "../entities/Employee";
import { EmployeeController } from "../controllers/employee-controller";






@Injectable({ providedIn: "root" })
export class EmployeeService {


    employeeSignal = signal<Employee | null>(null);
    employees = signal<Employee[]>([]);
    loading = signal(false);
    error = signal<string | null>(null);

    employeeController = inject(EmployeeController);


    loadEmployees(){
        this.loading.set(true);
        this.error.set(null);
        this.employeeController.getAllEmployees().subscribe({
            next: (employees) => {
                this.employees.set(employees);
                this.loading.set(false);
            },
            error: () => {
                this.error.set("Failed to load employees");
                this.loading.set(false);
            }
        });

    }


    loadEmployeeById(id: string) {
        this.loading.set(true);
        this.error.set(null);
        this.employeeController.getEmployeeById(id).subscribe({
            next: (employee) => {
                this.employeeSignal.set(employee);
                this.loading.set(false);
            },
            error: () => {
                this.error.set("Failed to load employee");
                this.loading.set(false);
            }
        });
    }

    getEmployeeById(id: string) {
        return this.employeeController.getEmployeeById(id);
    }


    loadEmployeesByCompanyId(companyId: string) {
        this.loading.set(true);
        this.error.set(null);
        const observable = this.employeeController.getAllEmployeesByCompanyId(companyId);
        observable.subscribe({
            next: (employees) => {
                this.employees.set(employees);
                if (!employees || employees.length === 0) {
                    this.error.set('No employees found for this company');
                }
                this.loading.set(false);
            },
            error: (err) => {
                if (err && (err.status === 404 || err.message === 'not found')) {
                    this.error.set('No employees found for this company');
                } else {
                    this.error.set("Failed to load employees for company");
                }
                this.loading.set(false);
            }
        });
        return observable;
    }

    loadUnemployedEmployees() {
        this.loading.set(true);
        this.error.set(null);
        this.employeeController.getAllEmployees().subscribe({
            next: (employees) => {
                this.employees.set(
                    employees.filter(e => !e.companyId || e.companyId.trim() === '')
                );
                this.loading.set(false);
            },
            error: () => {
                this.error.set("Failed to load unemployed employees");
                this.loading.set(false);
            }
        });
    }

    createEmployee(employee: Employee) {
        this.error.set(null);
        this.employeeController.createEmployee(employee).subscribe({
            next: (newEmployee) => {
                if (newEmployee) {
                    this.employees.update(list => [...list, newEmployee]);
                } else if (employee.companyId) {
                    this.loadEmployeesByCompanyId(employee.companyId);
                }
            },
            error: () => {
                this.error.set("Failed to create employee");
            }
        });
    }


    updateEmployee(id: string, employee: Employee, companyId?: string) {
        this.loading.set(true);
        this.error.set(null);

        this.employeeController.updateEmployee(id, employee).subscribe({
            next: (updatedEmployee) => {
                this.employees.update(list => {
                    const nextList = list.map(e => (e.id === id ? updatedEmployee : e));
                    if (companyId && updatedEmployee.companyId !== companyId) {
                        return nextList.filter(e => e.id !== id);
                    }
                    return nextList;
                });
                this.loading.set(false);
                
            },
            error: () => {
                this.error.set("Failed to update employee");
                this.loading.set(false);
            }
        });
    }


    deleteEmployee(id: string, companyId?: string) {
        this.loading.set(true);
        this.error.set(null);
        this.employeeController.deleteEmployee(id).subscribe({
            next: () => {
                if (companyId) {
                    this.loadEmployeesByCompanyId(companyId);
                } else {
                    this.employees.update(list => list.filter(e => e.id !== id));
                    this.loading.set(false);
                }
            },
            error: () => {
                this.error.set("Failed to delete employee");
                this.loading.set(false);
            }
        });
    }








}