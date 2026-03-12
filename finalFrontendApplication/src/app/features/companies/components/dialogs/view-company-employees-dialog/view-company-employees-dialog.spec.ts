import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCompanyEmployeesDialog } from './view-company-employees-dialog';

describe('ViewCompanyEmployeesDialog', () => {
  let component: ViewCompanyEmployeesDialog;
  let fixture: ComponentFixture<ViewCompanyEmployeesDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewCompanyEmployeesDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ViewCompanyEmployeesDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
