import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';


@Component({
  selector: 'app-create-company-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  templateUrl: './create-company-dialog.html',
  styleUrl: './create-company-dialog.css',
})
export class CreateCompanyDialog {
  dialogRef = inject(MatDialogRef<CreateCompanyDialog>);
  data = inject(MAT_DIALOG_DATA);

  formData = { name: '', address: '' };

  submit(): void {
    this.dialogRef.close(this.formData);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
