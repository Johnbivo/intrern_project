import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnassignEmployeeDialog } from './unassign-employee-dialog';

describe('UnassignEmployeeDialog', () => {
  let component: UnassignEmployeeDialog;
  let fixture: ComponentFixture<UnassignEmployeeDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UnassignEmployeeDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(UnassignEmployeeDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
