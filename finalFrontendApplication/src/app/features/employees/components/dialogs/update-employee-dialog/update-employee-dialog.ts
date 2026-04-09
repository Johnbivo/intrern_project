import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions } from '@angular/material/dialog';

@Component({
  selector: 'app-update-employee-dialog',
  imports: [MatInputModule, MatButtonModule, FormsModule, NgIf, MatFormFieldModule, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions],
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
  showValidationErrors = false;
  emailTouched = false;

  private isValidEmail(value: string): boolean {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(value);
  }

  isEmailInvalid(): boolean {
    return !this.isValidEmail(this.formData.email.trim());
  }

  showEmailError(): boolean {
    return this.isEmailInvalid() && (this.emailTouched || this.showValidationErrors);
  }

  markEmailTouched(): void {
    this.emailTouched = true;
  }

  canSubmit(): boolean {
    const name = this.formData.name.trim();
    const email = this.formData.email.trim();
    return name.length > 0 && this.isValidEmail(email);
  }

  submit(): void {
    this.showValidationErrors = true;

    if (!this.canSubmit()) {
      return;
    }

    this.formData = {
      ...this.formData,
      name: this.formData.name.trim(),
      email: this.formData.email.trim(),
    };

    this.dialogRef.close(this.formData);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
