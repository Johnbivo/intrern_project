import { Component,inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-delete-device-dialog',
  imports: [MatDialogModule, MatButtonModule],
  templateUrl: './delete-device-dialog.html',
  styleUrl: './delete-device-dialog.css',
})
export class DeleteDeviceDialog {


  dialogRef = inject(MatDialogRef<DeleteDeviceDialog>);
  data = inject(MAT_DIALOG_DATA);
  
  submit(): void {
    this.dialogRef.close(true);
  }

  cancel(): void {
    this.dialogRef.close(false);
  }
}
