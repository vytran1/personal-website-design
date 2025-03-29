import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Role } from '../../../../../models/role/role.model';

@Component({
  selector: 'app-owned-role-table',
  standalone: true,
  imports: [],
  templateUrl: './owned-role-table.component.html',
  styleUrl: './owned-role-table.component.css',
})
export class OwnedRoleTableComponent {
  @Input()
  ownedRoles: Role[] = [];

  @Input()
  userId!: number;

  @Output()
  deleteUserRoleEmitter: EventEmitter<Role> = new EventEmitter();

  constructor() {}

  deleteUserRole(role: Role) {
    this.deleteUserRoleEmitter.emit(role);
  }
}
