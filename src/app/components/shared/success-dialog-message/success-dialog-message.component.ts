import { Component, Inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';

@Component({
  selector: 'app-success-dialog-message',
  standalone: true,
  imports: [MatDialogModule],
  templateUrl: './success-dialog-message.component.html',
  styleUrl: './success-dialog-message.component.css',
})
export class SuccessDialogMessageComponent {
  constructor(
    public dialogRef: MatDialogRef<SuccessDialogMessageComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) {}

  closeDialog(): void {
    this.dialogRef.close('ok');
  }
}
