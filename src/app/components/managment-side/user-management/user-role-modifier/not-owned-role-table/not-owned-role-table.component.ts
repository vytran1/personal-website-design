import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Role } from '../../../../../models/role/role.model';

@Component({
  selector: 'app-not-owned-role-table',
  standalone: true,
  imports: [],
  templateUrl: './not-owned-role-table.component.html',
  styleUrl: './not-owned-role-table.component.css',
})
export class NotOwnedRoleTableComponent {
  @Input()
  notOwnedRoles: Role[] = [];

  @Input()
  userId!: number;

  @Output()
  roleEmitter: EventEmitter<Role> = new EventEmitter();

  constructor() {}

  addRole(role: Role) {
    this.roleEmitter.emit(role);
  }
}
