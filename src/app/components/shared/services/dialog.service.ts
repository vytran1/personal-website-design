import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogMessageComponent } from '../error-dialog-message/error-dialog-message.component';
import { SuccessDialogMessageComponent } from '../success-dialog-message/success-dialog-message.component';
import { Observable } from 'rxjs';
import { WarningDialogMessageComponent } from '../warning-dialog-message/warning-dialog-message.component';

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  constructor(private dialog: MatDialog) {}

  openErrorDialog(errorMessage: string): Observable<any> {
    const dialogRef = this.dialog.open(ErrorDialogMessageComponent, {
      data: { message: errorMessage },
      width: '400px',
      maxHeight: '80vh',
      autoFocus: false,
      panelClass: 'custom-dialog',
      backdropClass: 'dialog-backdrop',
    });

    dialogRef.afterOpened().subscribe(() => {
      const matDialogElement = document.querySelector(
        '.mat-mdc-dialog-container'
      ) as HTMLElement;
      if (matDialogElement) {
        matDialogElement.style.position = 'fixed';
        matDialogElement.style.top = '50%';
        matDialogElement.style.left = '50%';
        matDialogElement.style.transform = 'translate(-50%, -50%)';
      }
    });

    return dialogRef.afterClosed();
  }

  openSuccessDialog(successMessage: string): Observable<any> {
    const dialogRef = this.dialog.open(SuccessDialogMessageComponent, {
      data: { message: successMessage },
      width: '400px',
      maxHeight: '80vh',
      autoFocus: false,
      panelClass: 'custom-dialog',
      backdropClass: 'dialog-backdrop',
    });

    dialogRef.afterOpened().subscribe(() => {
      const matDialogElement = document.querySelector(
        '.mat-mdc-dialog-container'
      ) as HTMLElement;
      if (matDialogElement) {
        matDialogElement.style.position = 'fixed';
        matDialogElement.style.top = '50%';
        matDialogElement.style.left = '50%';
        matDialogElement.style.transform = 'translate(-50%, -50%)';
      }
    });

    return dialogRef.afterClosed();
  }

  openDialog(component: any, message: string): Observable<any> {
    const dialogRef = this.dialog.open(component, {
      data: { message },
      width: '400px',
      maxHeight: '80vh',
      autoFocus: false,
      panelClass: 'custom-dialog',
      backdropClass: 'dialog-backdrop',
    });

    dialogRef.afterOpened().subscribe(() => {
      const matDialogElement = document.querySelector(
        '.mat-mdc-dialog-container'
      ) as HTMLElement;
      if (matDialogElement) {
        matDialogElement.style.position = 'fixed';
        matDialogElement.style.top = '50%';
        matDialogElement.style.left = '50%';
        matDialogElement.style.transform = 'translate(-50%, -50%)';
      }
    });

    return dialogRef.afterClosed();
  }

  openWarningDialog(warningMessage: string): Observable<any> {
    return this.openDialog(WarningDialogMessageComponent, warningMessage);
  }
}
