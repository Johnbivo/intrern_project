import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeMenuCard } from './employee-menu-card';

describe('EmployeeMenuCard', () => {
  let component: EmployeeMenuCard;
  let fixture: ComponentFixture<EmployeeMenuCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeMenuCard],
    }).compileComponents();

    fixture = TestBed.createComponent(EmployeeMenuCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
