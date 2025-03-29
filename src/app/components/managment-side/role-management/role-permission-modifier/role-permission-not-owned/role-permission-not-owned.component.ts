import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Permission } from '../../../../../models/permission/permission.model';

@Component({
  selector: 'app-role-permission-not-owned',
  standalone: true,
  imports: [],
  templateUrl: './role-permission-not-owned.component.html',
  styleUrl: './role-permission-not-owned.component.css',
})
export class RolePermissionNotOwnedComponent {
  @Input()
  permissionInTheSystem: Permission[] = [];

  @Output()
  addPermissionEmitter: EventEmitter<Permission> = new EventEmitter();

  addPermission(permission: Permission) {
    this.addPermissionEmitter.emit(permission);
  }
}
