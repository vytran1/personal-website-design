import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthServiceService } from '../services/authentication/services/auth-service.service';
import { map } from 'rxjs';

export const authSuperAdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthServiceService);
  const router = inject(Router);

  console.log('Admin guard');

  authService.loadToken();

  return authService.isLogged().pipe(
    map((isLoggedIn) => {
      if (isLoggedIn && authService.isSuperAdminAccount()) {
        return true;
      }

      router.navigateByUrl('/forbidden-authorization');
      return false;
    })
  );
};
