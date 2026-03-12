import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEmployeeDialog } from './create-employee-dialog';

describe('CreateEmployeeDialog', () => {
  let component: CreateEmployeeDialog;
  let fixture: ComponentFixture<CreateEmployeeDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateEmployeeDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateEmployeeDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
