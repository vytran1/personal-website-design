import { Routes } from '@angular/router';
import { CandidateComponent } from './components/candidate/candidate.component';
import { DockerTestComponent } from './components/docker-test/docker-test.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { LoginComponent } from './components/login/login.component';
import { GoogleLoginHandlerComponent } from './components/login/google-login-handler/google-login-handler.component';
import { ManagmentSideComponent } from './components/managment-side/managment-side.component';
import { ForbiddenPageComponent } from './components/forbidden-page/forbidden-page.component';
import { authAdminGuard } from './guard/auth-admin.guard';
import { UserManagementComponent } from './components/managment-side/user-management/user-management.component';
import { RoleManagementComponent } from './components/managment-side/role-management/role-management.component';
import { authSuperAdminGuard } from './guard/auth-super-admin.guard';
import { UserRoleModifierComponent } from './components/managment-side/user-management/user-role-modifier/user-role-modifier.component';
import { RoleCreatingFormComponent } from './components/managment-side/role-management/role-creating-form/role-creating-form.component';
import { RolePermissionModifierComponent } from './components/managment-side/role-management/role-permission-modifier/role-permission-modifier.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'login/oauth2/callback', component: GoogleLoginHandlerComponent },
  { path: 'candidate', component: CandidateComponent },
  { path: 'docker', component: DockerTestComponent },
  {
    path: 'admin',
    component: ManagmentSideComponent,
    canActivate: [authAdminGuard],
    children: [
      {
        path: 'users',
        component: UserManagementComponent,
        children: [],
      },
      {
        path: 'role-modifier/:userId',
        component: UserRoleModifierComponent,
      },
      {
        path: 'roles',
        component: RoleManagementComponent,
        canActivate: [authSuperAdminGuard],
      },
      {
        path: 'roles/create',
        component: RoleCreatingFormComponent,
        canActivate: [authSuperAdminGuard],
      },
      {
        path: 'roles/modify-permission/:roleId',
        component: RolePermissionModifierComponent,
      },
    ],
  },
  { path: 'forbidden-authorization', component: ForbiddenPageComponent },
  { path: '**', component: NotFoundComponent },
];
