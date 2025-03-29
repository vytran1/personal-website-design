import { Component, OnDestroy, OnInit } from '@angular/core';
import { Role } from '../../../../models/role/role.model';
import { UserService } from '../../../../services/user.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogMessageComponent } from '../../../shared/error-dialog-message/error-dialog-message.component';
import { OwnedRoleTableComponent } from './owned-role-table/owned-role-table.component';
import { NotOwnedRoleTableComponent } from './not-owned-role-table/not-owned-role-table.component';
import { RoleService } from '../../../../services/role.service';

@Component({
  selector: 'app-user-role-modifier',
  standalone: true,
  imports: [OwnedRoleTableComponent, NotOwnedRoleTableComponent],
  templateUrl: './user-role-modifier.component.html',
  styleUrl: './user-role-modifier.component.css',
})
export class UserRoleModifierComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];

  ownedRoles: Role[] = [];

  notOwnedRoles: Role[] = [];

  userId!: number;

  constructor(
    private userService: UserService,
    private roleService: RoleService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.userId = this.activatedRoute.snapshot.params['userId'];
    //Call API Get All Roles Belong To The User needing to modify
    this.subscriptions.push(
      this.userService.listAllRoleOfOneUser(this.userId).subscribe({
        next: (response) => {
          console.log('Response from List All Role Of One User');
          console.log(response);
          this.ownedRoles = response.body;
        },
        error: (err) => {
          console.log('Error from List All Role Of One User');
          console.log(err);
          if (err.status == 404) {
            this.openErrorDialog('Not exist user with the given id');
          }
        },
        complete: () => {
          console.log('Complete from List All Role Of One User');
        },
      })
    );

    //Call API Get ALL Roles In System
    this.subscriptions.push(
      this.roleService.listAllRoles().subscribe({
        next: (response) => {
          console.log('Response from List All Roles');
          console.log(response);
          this.notOwnedRoles = response.body;
        },
        error: (err) => {
          console.log('Error from List All Roles');
          console.log(err);
        },
        complete: () => {
          console.log('Complete from List All Roles');
        },
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  openErrorDialog(errorMessage: string) {
    const dialogRef = this.dialog.open(ErrorDialogMessageComponent, {
      data: { message: errorMessage },
      width: '400px',
      maxHeight: '80vh',
      autoFocus: false,
      panelClass: 'custom-dialog',
      backdropClass: 'dialog-backdrop', // Đưa hộp thoại lên giữa
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

    dialogRef.afterClosed().subscribe(() => {
      this.router.navigate(['/admin/users']);
    });
  }

  isOwnedThatRole(role: Role) {
    const isContained = this.ownedRoles.some(
      (ownedRole) => ownedRole.id === role.id
    );
    console.log('Is Contained', isContained);
    return isContained;
  }

  addNewRole(role: Role) {
    //Check if this role is owned
    const isOwned = this.isOwnedThatRole(role);

    if (isOwned) {
      console.log('This role has existed');
      this.openErrorDialogForAddNewRoleFunction('This role has existed');
    } else {
      console.log('Can add');
      this.ownedRoles.push(role);
    }
  }

  deleteRole(role: Role) {
    const index = this.ownedRoles.findIndex(
      (ownedRole) => ownedRole.id === role.id
    );
    if (index !== -1) {
      this.ownedRoles.splice(index, 1);
    }
  }

  openErrorDialogForAddNewRoleFunction(errorMessage: string) {
    const dialogRef = this.dialog.open(ErrorDialogMessageComponent, {
      data: { message: errorMessage },
      width: '400px',
      maxHeight: '80vh',
      autoFocus: false,
      panelClass: 'custom-dialog',
      backdropClass: 'dialog-backdrop', // Đưa hộp thoại lên giữa
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
  }

  updateNewRoles() {
    console.log('New Roles', this.ownedRoles);

    this.subscriptions.push(
      this.userService
        .updateRolesOfOneUser(this.userId, this.ownedRoles)
        .subscribe({
          next: (response) => {
            console.log('Response From Update New Roles API');
            console.log(response);
            window.alert('Update User Roles Successfully');
            this.ownedRoles = response.body;
          },
          error: (err) => {
            console.log('Error From Update New Roles API');
            console.log(err);
            if (err.status == 404) {
              this.openErrorDialog('Not exist user with the given id');
            }
          },
          complete: () => {
            console.log('Update New Roles API Complete');
          },
        })
    );
  }
}
