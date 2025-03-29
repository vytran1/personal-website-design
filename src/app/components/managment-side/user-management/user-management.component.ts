import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from '../../../models/user/user.model';
import { UserService } from '../../../services/user.service';
import { AuthServiceService } from '../../../services/authentication/services/auth-service.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
})
export class UserManagementComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  users: User[] = [];
  isSuperAdmin: boolean = false;

  constructor(
    private userService: UserService,
    private authService: AuthServiceService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    //Call API To Get List Users

    this.authService.loadToken();
    this.isSuperAdmin = this.authService.isSuperAdminAccount();

    this.subscriptions.push(
      this.userService.listAllUserEndpoint().subscribe({
        next: (response) => {
          console.log('Response From List All Users API');
          console.log(response);
          this.users = response.body;
          console.log(this.users);
        },
        error: (err) => {
          console.log('Error From List All Users API');
          console.log(err);
        },
        complete: () => {
          console.log('Complete Call List All Users API');
        },
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  // isSuperAdmin() {
  //   return this.authService.isSuperAdminAccount();
  // }

  redirectToUserRoleModifer(userId: number) {
    this.router.navigate(['/admin/role-modifier', userId]);
  }
}
