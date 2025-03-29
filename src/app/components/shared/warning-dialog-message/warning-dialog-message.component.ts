import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';

@Component({
  selector: 'app-warning-dialog-message',
  standalone: true,
  imports: [MatDialogModule],
  templateUrl: './warning-dialog-message.component.html',
  styleUrl: './warning-dialog-message.component.css',
})
export class WarningDialogMessageComponent {
  constructor(
    public dialogRef: MatDialogRef<WarningDialogMessageComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) {}

  onConfirm() {
    this.dialogRef.close(true);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
