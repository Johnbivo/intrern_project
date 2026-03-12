import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { EmployeeController } from '../controllers/employee-controller';
import { Employee } from '../entities/Employee';
import { EmployeeService } from './employee-service';

describe('EmployeeService', () => {
	let service: EmployeeService;

	const employeeFactory = (overrides: Partial<Employee> = {}): Employee => ({
		id: 'emp-1',
		name: 'John Doe',
		email: 'john@example.com',
		companyId: '',
		...overrides,
	});

	const employeeControllerMock = {

		getAllEmployees: vi.fn(),
		getEmployeeById: vi.fn(),
		getAllEmployeesByCompanyId: vi.fn(),
		createEmployee: vi.fn(),
		updateEmployee: vi.fn(),
		deleteEmployee: vi.fn(),
	};

	beforeEach(() => {
		vi.clearAllMocks();

		TestBed.configureTestingModule({
			providers: [
				EmployeeService,
				{ provide: EmployeeController, useValue: employeeControllerMock },
			],
		});

		service = TestBed.inject(EmployeeService);
		service.employeeSignal.set(null);
		service.employees.set([]);
		service.loading.set(false);
		service.error.set(null);
	});

	it('should create', () => {
		expect(service).toBeTruthy();
	});

	it('loadEmployees should set employees on success', () => {
		const employees = [employeeFactory()];
		employeeControllerMock.getAllEmployees.mockReturnValue(of(employees));

		service.loadEmployees();

		expect(employeeControllerMock.getAllEmployees).toHaveBeenCalledTimes(1);
		expect(service.employees()).toEqual(employees);
		expect(service.loading()).toBe(false);
		expect(service.error()).toBeNull();
	});

	it('loadEmployees should set error on failure', () => {
		employeeControllerMock.getAllEmployees.mockReturnValue(
			throwError(() => new Error('boom'))
		);

		service.loadEmployees();

		expect(service.error()).toBe('Failed to load employees');
		expect(service.loading()).toBe(false);
	});

	it('createEmployee should append new employee on success', () => {
		const existing = employeeFactory({ id: 'emp-1' });
		const created = employeeFactory({ id: 'emp-2', name: 'Jane Doe' });
		service.employees.set([existing]);
		employeeControllerMock.createEmployee.mockReturnValue(of(created));

		service.createEmployee(created);

		expect(employeeControllerMock.createEmployee).toHaveBeenCalledWith(created);
		expect(service.employees()).toEqual([existing, created]);
		expect(service.error()).toBeNull();
	});

	it('updateEmployee should replace employee in list', () => {
		const existing = employeeFactory({ id: 'emp-1', name: 'Old Name' });
		const updated = employeeFactory({ id: 'emp-1', name: 'New Name' });
		service.employees.set([existing]);
		employeeControllerMock.updateEmployee.mockReturnValue(of(updated));

		service.updateEmployee(existing.id, updated);

		expect(employeeControllerMock.updateEmployee).toHaveBeenCalledWith(existing.id, updated);
		expect(service.employees()).toEqual([updated]);
		expect(service.loading()).toBe(false);
	});



	


	it('deleteEmployee should remove employee locally when companyId is not provided', () => {
		const one = employeeFactory({ id: 'emp-1' });
		const two = employeeFactory({ id: 'emp-2' });

		service.employees.set([one, two]);


		employeeControllerMock.deleteEmployee.mockReturnValue(of(void 0));

		service.deleteEmployee('emp-1');


		expect(employeeControllerMock.deleteEmployee).toHaveBeenCalledWith('emp-1');
		expect(service.employees()).toEqual([two]);
		expect(service.loading()).toBe(false);
	});
});
