import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDeviceDialog } from './create-device-dialog';

describe('CreateDeviceDialog', () => {
  let component: CreateDeviceDialog;
  let fixture: ComponentFixture<CreateDeviceDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateDeviceDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateDeviceDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
