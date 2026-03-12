import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteCompanyDialog } from './delete-company-dialog';

describe('DeleteCompanyDialog', () => {
  let component: DeleteCompanyDialog;
  let fixture: ComponentFixture<DeleteCompanyDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteCompanyDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(DeleteCompanyDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
