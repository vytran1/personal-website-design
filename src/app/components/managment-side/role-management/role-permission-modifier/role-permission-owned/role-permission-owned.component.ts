import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Permission } from '../../../../../models/permission/permission.model';

@Component({
  selector: 'app-role-permission-owned',
  standalone: true,
  imports: [],
  templateUrl: './role-permission-owned.component.html',
  styleUrl: './role-permission-owned.component.css',
})
export class RolePermissionOwnedComponent {
  @Input()
  ownedPermission: Permission[] = [];

  @Output()
  deletePermissionEmitter: EventEmitter<Permission> = new EventEmitter();

  deletePermission(permission: Permission) {
    this.deletePermissionEmitter.emit(permission);
  }
}
