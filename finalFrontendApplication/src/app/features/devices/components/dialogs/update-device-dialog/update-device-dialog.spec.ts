import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDeviceDialog } from './update-device-dialog';

describe('UpdateDeviceDialog', () => {
  let component: UpdateDeviceDialog;
  let fixture: ComponentFixture<UpdateDeviceDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateDeviceDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(UpdateDeviceDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
