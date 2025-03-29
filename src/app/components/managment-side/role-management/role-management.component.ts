import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { RoleService } from '../../../services/role.service';
import { Role } from '../../../models/role/role.model';
import { DialogService } from '../../shared/services/dialog.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthServiceService } from '../../../services/authentication/services/auth-service.service';

@Component({
  selector: 'app-role-management',
  standalone: true,
  imports: [],
  templateUrl: './role-management.component.html',
  styleUrl: './role-management.component.css',
})
export class RoleManagementComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  roles: Role[] = [];
  isProcessingDelete: boolean = false;

  constructor(
    private roleService: RoleService,
    private dialogService: DialogService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthServiceService
  ) {}

  ngOnInit(): void {
    this.authService.loadToken();

    this.subscriptions.push(
      this.roleService.listAllRoles().subscribe({
        next: (response) => {
          console.log('Response From List All Role API');
          console.log(response);
          this.roles = response.body;
        },
        error: (err) => {
          console.log('Error From List All Role API');
          console.log(err);
        },
        complete: () => {
          console.log('Complete calling list all role API');
        },
      })
    );
  }

  openWarningDialog(roleId: number) {
    this.dialogService
      .openWarningDialog('Are you sure you want to delete this role?')
      .subscribe((result) => {
        if (result) {
          console.log('Deleting Role with id', roleId);
          this.deleteRole(roleId);
        } else {
          console.log('Cancel Delete Operation');
        }
      });
  }

  deleteRole(roleId: number) {
    console.log('Role Id', roleId);
    this.isProcessingDelete = true;
    this.subscriptions.push(
      this.roleService.deleteRole(roleId).subscribe({
        next: (response) => {
          console.log('Response From Delete Role API');
          console.log(response);

          this.dialogService
            .openSuccessDialog('Success Fully Delete Role')
            .subscribe((result) => {
              console.log('Dialog Close With Result', result);
              if (result == 'ok') {
                this.successHandler();
              }
            });
        },
        error: (err) => {
          console.log('Error From Delete Role API');
          console.log(err);
          this.isProcessingDelete = false;

          if (err.status === 404) {
            this.dialogService.openErrorDialog(
              'Not Found The Role With The given id'
            );
          } else {
            this.dialogService.openErrorDialog('Interval Server Error');
          }
        },
        complete: () => {
          console.log('Complete calling delete role API');
          this.isProcessingDelete = false;
        },
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  successHandler() {
    console.log('Success Delete Role');
    window.location.reload();
  }

  redirectToCreateRoleForm() {
    this.router.navigate(['create'], { relativeTo: this.activatedRoute });
  }

  redirectToRolePermissionModifier(roleId: number) {
    this.router.navigate(['modify-permission', roleId], {
      relativeTo: this.activatedRoute,
    });
  }
}
