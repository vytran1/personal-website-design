import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError, from, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthServiceService } from '../services/authentication/services/auth-service.service';
import { RefreshTokenService } from '../services/authentication/services/refresh-token.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';

let isRefreshing = false;
let refreshTokenSubject: string | null = null;

export const authDefaultInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthServiceService);
  const refreshTokenService = inject(RefreshTokenService);
  const router = inject(Router);
  const jwtHelper = new JwtHelperService();

  authService.loadToken();
  refreshTokenService.loadRefreshToken();

  const accessToken = authService.getToken();
  const refreshToken = refreshTokenService.getRefreshToken();
  const currentLoginUsername = authService.getCurrentLoginUsername();

  // Bỏ qua các request login và refresh token
  if (
    req.url.includes(`${authService.host}/auth/token`) ||
    req.url.includes(`${authService.host}/auth/refresh/token`) ||
    req.url.includes(`${authService.host}/auth/google/link`) ||
    req.url.includes(`${authService.host}/auth/google/callback`)
  ) {
    return next(req);
  }

  // Kiểm tra token hiện tại có không và có hết hạn không
  if (accessToken && jwtHelper.isTokenExpired(accessToken)) {
    console.log('Access Token đã hết hạn');

    // Đảm bảo chỉ refresh token một lần
    if (!isRefreshing) {
      isRefreshing = true;
      refreshTokenSubject = null;

      console.log('🌐 Đang gọi API refresh token...');

      return from(refreshTokenService.getNewToken(currentLoginUsername!)).pipe(
        switchMap((response: any) => {
          console.log('Làm mới Access Token thành công', response);

          const newAccessToken = response.body.accessToken;
          const newRefreshToken = response.body.refreshToken;

          authService.saveToken(newAccessToken);
          refreshTokenService.saveRefreshToken(newRefreshToken);

          refreshTokenSubject = newAccessToken;
          isRefreshing = false;

          return next(
            req.clone({
              setHeaders: { Authorization: `Bearer ${newAccessToken}` },
            })
          );
        }),
        catchError((err) => {
          console.error('Lỗi khi làm mới token', err);
          isRefreshing = false;
          authService.removeToken();
          refreshTokenService.removeRefreshToken();
          router.navigate(['/login']);
          return throwError(() => new Error('Token Expired'));
        })
      );
    } else {
      // Đợi token mới nếu đang trong quá trình làm mới
      return of(refreshTokenSubject).pipe(
        switchMap((newToken) => {
          if (newToken) {
            return next(
              req.clone({
                setHeaders: { Authorization: `Bearer ${newToken}` },
              })
            );
          }
          return throwError(() => new Error('Token expired'));
        }),
        catchError(() => {
          authService.removeToken();
          refreshTokenService.removeRefreshToken();
          router.navigate(['/login']);
          return throwError(() => new Error('Token expired'));
        })
      );
    }
  }

  // Trường hợp token vẫn còn hiệu lực
  if (accessToken) {
    return next(
      req.clone({
        setHeaders: { Authorization: `Bearer ${accessToken}` },
      })
    ).pipe(
      catchError((error) => {
        if (error.status === 401) {
          authService.removeToken();
          refreshTokenService.removeRefreshToken();
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  // Trường hợp không có access token
  authService.removeToken();
  refreshTokenService.removeRefreshToken();
  router.navigate(['/login']);
  return throwError(() => new Error('Token expired or missing'));
};
