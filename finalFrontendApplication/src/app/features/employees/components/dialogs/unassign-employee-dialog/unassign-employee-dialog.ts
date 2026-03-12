import { Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle,
} from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

interface UnassignEmployeeDialogData {
  employeeName: string;
  companyName: string;
}

@Component({
  selector: 'app-unassign-employee-dialog',
  imports: [MatDialogTitle, MatDialogContent, MatDialogActions, MatButtonModule],
  templateUrl: './unassign-employee-dialog.html',
  styleUrl: './unassign-employee-dialog.css',
})
export class UnassignEmployeeDialog {
  dialogRef = inject(MatDialogRef<UnassignEmployeeDialog>);
  data: UnassignEmployeeDialogData = inject(MAT_DIALOG_DATA);

  confirm(): void {
    this.dialogRef.close(true);
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
