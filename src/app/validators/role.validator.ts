import { AbstractControl, ValidationErrors } from '@angular/forms';
import { RoleService } from '../services/role.service';
import { catchError, first, map, Observable, of } from 'rxjs';

export function isRoleNameUnique(roleService: RoleService) {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) {
      return of(null); // Nếu giá trị rỗng thì không cần validate
    }

    return roleService.checkUniqueOfName(control.value).pipe(
      map((isDuplicate: boolean) =>
        isDuplicate ? { roleNameExist: true } : null
      ),
      catchError((error) => {
        console.log('Error From Call API Check Unique Role Name', error);
        return of(null);
      }),
      first()
    );
  };
}
