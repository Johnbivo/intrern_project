import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCompanyDevicesDialog } from './view-company-devices-dialog';

describe('ViewCompanyDevicesDialog', () => {
  let component: ViewCompanyDevicesDialog;
  let fixture: ComponentFixture<ViewCompanyDevicesDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewCompanyDevicesDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ViewCompanyDevicesDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
