import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCompanyDialog } from './create-company-dialog';

describe('CreateCompanyDialog', () => {
  let component: CreateCompanyDialog;
  let fixture: ComponentFixture<CreateCompanyDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateCompanyDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateCompanyDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
