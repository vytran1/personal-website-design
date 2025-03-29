import { Component, OnDestroy, OnInit } from '@angular/core';
import { Permission } from '../../../../models/permission/permission.model';
import { Subscription } from 'rxjs';
import { PermissionService } from '../../../../services/permission.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogService } from '../../../shared/services/dialog.service';
import { RoleService } from '../../../../services/role.service';
import { RolePermissionOwnedComponent } from './role-permission-owned/role-permission-owned.component';
import { RolePermissionNotOwnedComponent } from './role-permission-not-owned/role-permission-not-owned.component';

@Component({
  selector: 'app-role-permission-modifier',
  standalone: true,
  imports: [RolePermissionOwnedComponent, RolePermissionNotOwnedComponent],
  templateUrl: './role-permission-modifier.component.html',
  styleUrl: './role-permission-modifier.component.css',
})
export class RolePermissionModifierComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  ownedPermission: Permission[] = [];
  notOwnedPermission: Permission[] = [];
  roleId!: number;
  isProcessing: boolean = false;

  constructor(
    private permissionService: PermissionService,
    private roleService: RoleService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private dialogService: DialogService
  ) {}

  ngOnInit(): void {
    this.roleId = this.activatedRoute.snapshot.params['roleId'];

    if (this.roleId) {
      this.subscriptions.push(
        this.roleService.listAllRoleOfOneRole(this.roleId).subscribe({
          next: (response) => {
            console.log('Response From List All Role Of One Role');
            console.log(response);
            this.ownedPermission = response.body;
          },
          error: (err) => {
            console.log('Error From List All Role Of One Role');
            console.log(err);
            if (err.status === 404) {
              this.dialogService
                .openErrorDialog('Not Found Role With The Given Id')
                .subscribe((result) => this.notFoundHandler());
            }
          },
          complete: () => {
            console.log('Complete From List All Role Of One Role');
          },
        })
      );

      this.subscriptions.push(
        this.permissionService.listAllPermissionInTheSystem().subscribe({
          next: (response) => {
            console.log('Response From List All Permission In The System');
            console.log(response);
            this.notOwnedPermission = response.body;
          },
          error: (err) => {
            console.log('Error From List All Permission In The System');
            console.log(err);
          },
          complete: () => {
            console.log('Complete From List All Permission In The System');
          },
        })
      );
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  notFoundHandler() {
    this.router.navigateByUrl('/admin/roles');
  }

  deletePermissionHandler(permission: Permission) {
    const index = this.ownedPermission.findIndex((p) => p.id === permission.id);

    if (index != -1) {
      this.ownedPermission.splice(index, 1);
    }
  }

  addPermissionHandler(permission: Permission) {
    const isContained = this.ownedPermission.some(
      (p) => p.id === permission.id
    );

    if (isContained) {
      this.dialogService.openErrorDialog(
        'This role has already owned this permission'
      );
    } else {
      this.ownedPermission.push(permission);
    }
  }

  updatePermissionOfRole() {
    console.log(this.ownedPermission);
    this.isProcessing = true;
    this.subscriptions.push(
      this.roleService
        .updatePermissionOfOneRole(this.roleId, this.ownedPermission)
        .subscribe({
          next: (response) => {
            console.log('Response From Update Permissio Of One Role');
            console.log(response);
            this.isProcessing = false;
            this.dialogService.openSuccessDialog(
              'Succcessfully Update Permission Of One Role'
            );
            this.ownedPermission = response.body;
          },
          error: (err) => {
            console.log('Error From Update Permission Of One Role');
            console.log(err);
            this.isProcessing = false;
          },
          complete: () => {
            this.isProcessing = false;
          },
        })
    );
  }
}
