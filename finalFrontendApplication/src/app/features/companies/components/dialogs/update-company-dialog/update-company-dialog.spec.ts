import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCompanyDialog } from './update-company-dialog';

describe('UpdateCompanyDialog', () => {
  let component: UpdateCompanyDialog;
  let fixture: ComponentFixture<UpdateCompanyDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateCompanyDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateCompanyDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
