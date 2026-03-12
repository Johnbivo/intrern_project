import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-update-company-dialog',
  imports: [MatInputModule, MatButtonModule, FormsModule, MatFormFieldModule, MatSelectModule, MatDialogModule],
  templateUrl: './update-company-dialog.html',
  styleUrl: './update-company-dialog.css',
})
export class UpdateCompanyDialog {

  dialogRef = inject(MatDialogRef<UpdateCompanyDialog>);
  data = inject(MAT_DIALOG_DATA);

  formData = { name: this.data.name, address: this.data.address };

  submit(): void {
    this.dialogRef.close(this.formData);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
