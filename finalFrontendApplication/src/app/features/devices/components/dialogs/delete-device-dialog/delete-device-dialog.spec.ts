import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteDeviceDialog } from './delete-device-dialog';

describe('DeleteDeviceDialog', () => {
  let component: DeleteDeviceDialog;
  let fixture: ComponentFixture<DeleteDeviceDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteDeviceDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(DeleteDeviceDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
