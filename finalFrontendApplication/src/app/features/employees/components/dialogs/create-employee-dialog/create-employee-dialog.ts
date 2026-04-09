import { Component, inject } from '@angular/core';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions } from '@angular/material/dialog';

@Component({
  selector: 'app-create-employee-dialog',
  imports: [MatInputModule, MatButtonModule, FormsModule, NgIf, MatFormFieldModule, MatSelectModule, MatDialogModule],
  templateUrl: './create-employee-dialog.html',
  styleUrl: './create-employee-dialog.css',
})
export class CreateEmployeeDialog {

  dialogRef = inject(MatDialogRef<CreateEmployeeDialog>);
  data = inject(MAT_DIALOG_DATA);

  formData = { name: '', email: '' };
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
