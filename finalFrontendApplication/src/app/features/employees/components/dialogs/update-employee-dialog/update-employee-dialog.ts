import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions } from '@angular/material/dialog';

@Component({
  selector: 'app-update-employee-dialog',
  imports: [MatInputModule, MatButtonModule, FormsModule, MatFormFieldModule, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions],
  templateUrl: './update-employee-dialog.html',
  styleUrl: './update-employee-dialog.css',
})
export class UpdateEmployeeDialog {

  dialogRef = inject(MatDialogRef<UpdateEmployeeDialog>);
  data = inject(MAT_DIALOG_DATA);

  formData = { 
    id: this.data.id, 
    name: this.data.name, 
    email: this.data.email,
    companyId: this.data.companyId
  };

  submit(): void {
    this.dialogRef.close(this.formData);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
