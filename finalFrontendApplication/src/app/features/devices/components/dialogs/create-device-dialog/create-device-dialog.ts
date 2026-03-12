import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions } from '@angular/material/dialog';

@Component({
  selector: 'app-create-device-dialog',
  imports: [MatInputModule, MatButtonModule, FormsModule, MatFormFieldModule, MatSelectModule, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions],
  templateUrl: './create-device-dialog.html',
  styleUrl: './create-device-dialog.css',
})
export class CreateDeviceDialog {

  dialogRef = inject(MatDialogRef<CreateDeviceDialog>);
  data = inject(MAT_DIALOG_DATA);
  
  formData = {serialNumber: '', name: '', type: '' };

  submit(): void {
    this.dialogRef.close(this.formData);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
