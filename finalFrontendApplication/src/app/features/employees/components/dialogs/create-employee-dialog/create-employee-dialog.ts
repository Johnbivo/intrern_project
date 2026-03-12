import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions } from '@angular/material/dialog';

@Component({
  selector: 'app-create-employee-dialog',
  imports: [MatInputModule, MatButtonModule, FormsModule, MatFormFieldModule, MatSelectModule, MatDialogModule],
  templateUrl: './create-employee-dialog.html',
  styleUrl: './create-employee-dialog.css',
})
export class CreateEmployeeDialog {

  dialogRef = inject(MatDialogRef<CreateEmployeeDialog>);
  data = inject(MAT_DIALOG_DATA);

  formData = { name: '', email: '' };

  submit(): void {
    this.dialogRef.close(this.formData);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

}
