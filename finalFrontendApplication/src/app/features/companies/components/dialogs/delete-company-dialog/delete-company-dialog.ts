import { Component,inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-delete-company-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './delete-company-dialog.html',
  styleUrl: './delete-company-dialog.css',
})
export class DeleteCompanyDialog {


  dialogRef = inject(MatDialogRef<DeleteCompanyDialog>);
  data = inject(MAT_DIALOG_DATA);
  
  submit(): void {
    this.dialogRef.close(true);
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
