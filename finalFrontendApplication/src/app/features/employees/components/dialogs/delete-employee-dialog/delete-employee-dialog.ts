import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions } from '@angular/material/dialog';

@Component({
  selector: 'app-delete-employee-dialog',
  imports: [MatButtonModule, MatDialogModule, MatDialogTitle, MatDialogContent, MatDialogActions],
  templateUrl: './delete-employee-dialog.html',
  styleUrl: './delete-employee-dialog.css',
})
export class DeleteEmployeeDialog {

  dialogRef = inject(MatDialogRef<DeleteEmployeeDialog>);
  data = inject(MAT_DIALOG_DATA);

  confirm(): void {
    this.dialogRef.close(true);
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
