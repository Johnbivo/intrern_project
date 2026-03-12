import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Company } from '../../../../companies/entities/Company';
import { CompanyService } from '../../../../companies/services/company-service';
import { AssignEmployeeDialog } from './assign-employee-dialog';

describe('AssignEmployeeDialog', () => {


	let fixture: ComponentFixture<AssignEmployeeDialog>;
	let component: AssignEmployeeDialog;



	const companiesSignal = signal<Company[]>([]);

	const companyServiceMock = {
		companies: companiesSignal,
		loadCompanies: vi.fn(),
	};

	const dialogRefMock = {
		close: vi.fn(),
	};


    

	beforeEach(async () => {

		fakeCompanyService: 

		await TestBed.configureTestingModule({
			imports: [AssignEmployeeDialog],
			providers: [
				{ provide: CompanyService, useValue: companyServiceMock },
				{ provide: MatDialogRef, useValue: dialogRefMock },
				{
					provide: MAT_DIALOG_DATA,
					useValue: { employeeName: 'John Doe' },
				},
			],
		}).compileComponents();

		fixture = TestBed.createComponent(AssignEmployeeDialog);
		component = fixture.componentInstance;
	});



    


	it('should create', () => {
		expect(component).toBeTruthy();
	});


});
