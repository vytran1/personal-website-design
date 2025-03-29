import { Component, Inject } from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialogModule,
} from '@angular/material/dialog';
@Component({
  selector: 'app-error-dialog-message',
  standalone: true,
  imports: [MatDialogModule],
  templateUrl: './error-dialog-message.component.html',
  styleUrl: './error-dialog-message.component.css',
})
export class ErrorDialogMessageComponent {
  constructor(
    public dialogRef: MatDialogRef<ErrorDialogMessageComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) {}

  closeDialog(): void {
    this.dialogRef.close('ok');
  }
}
