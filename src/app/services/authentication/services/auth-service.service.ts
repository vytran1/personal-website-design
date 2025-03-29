import { Injectable } from '@angular/core';
import { environment } from '../../../environment/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Account } from '../model/account.model';
import { catchError, map, Observable, of } from 'rxjs';
import { RefreshTokenService } from './refresh-token.service';
@Injectable({
  providedIn: 'root',
})
export class AuthServiceService {
  host: string = environment.apiUrl;
  authPath: string = '/auth';
  jwtToken: string | null = null;
  jwtHelper = new JwtHelperService();
  currentLoginUsername: string | null = null;
  roles: string[] = [];

  constructor(
    private httpClient: HttpClient,
    private refreshTokenService: RefreshTokenService
  ) {}

  public login(account: Account): Observable<HttpResponse<any>> {
    const requestPath: string = this.host + this.authPath + '/token';

    return this.httpClient.post<HttpResponse<any>>(`${requestPath}`, account, {
      observe: 'response',
    });
  }

  public saveToken(token: string): void {
    console.log(
      'Information parse from token',
      this.jwtHelper.decodeToken(token)
    );

    const subValue = this.jwtHelper.decodeToken(token).sub;
    const currentLoginUsername = subValue.split(',')[1];

    localStorage.setItem('token', token);
    localStorage.setItem('currentLoginUsername', currentLoginUsername);
    this.jwtToken = token;
  }

  public loadToken(): void {
    this.jwtToken = localStorage.getItem('token');
    this.currentLoginUsername = localStorage.getItem('currentLoginUsername');

    if (this.jwtToken != null && this.jwtToken !== '') {
      if (this.jwtHelper.decodeToken(this.jwtToken).sub != null || '') {
        if (!this.jwtHelper.isTokenExpired(this.jwtToken)) {
          const decode = this.jwtHelper.decodeToken(this.jwtToken);
          const roles = decode.role || [];
          this.roles = roles.map((role: any) =>
            typeof role === 'string' ? role : role.name
          );
        }
      }
    }
  }

  public removeToken(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentLoginUsername');
    this.jwtToken = null;
    this.roles = [];
    this.currentLoginUsername = null;
  }

  public getToken(): string {
    if (this.jwtToken === null) {
      this.loadToken();
    }
    return this.jwtToken ?? '';
  }

  public getCurrentLoginUsername() {
    return this.currentLoginUsername;
  }

  public isLogged(): Observable<boolean> {
    this.loadToken();

    if (!this.jwtToken || this.jwtToken === '') {
      console.log('No having token');
      this.removeToken();
      return of(false);
    }

    if (!this.jwtHelper.decodeToken(this.jwtToken).sub) {
      console.log('No Having Sub');
      this.removeToken();
      return of(false);
    }

    // Kiểm tra nếu token đã hết hạn
    if (this.jwtHelper.isTokenExpired(this.jwtToken)) {
      console.log('Toke is expired try to refresh token...');
      this.refreshTokenService.loadRefreshToken();

      if (!this.currentLoginUsername) {
        this.removeToken();
        this.refreshTokenService.removeRefreshToken();
        return of(false);
      }

      return this.refreshTokenService
        .getNewToken(this.currentLoginUsername)
        .pipe(
          map((response) => {
            const accessToken = response.body.accessToken;
            const refreshToken = response.body.refreshToken;

            this.removeToken();
            this.refreshTokenService.removeRefreshToken();

            this.saveToken(accessToken);
            this.refreshTokenService.saveRefreshToken(refreshToken);

            this.loadToken();

            return true;
          }),
          catchError((err) => {
            console.log('Error From isLogged When Refresh Token', err);
            this.removeToken();
            return of(false);
          })
        );
    }

    return of(true);
  }

  public isAdminAccount(): boolean {
    console.log('Check Admin Role');
    console.log(this.roles);
    return this.roles.includes('ADMIN');
  }

  public isNormalAccount(): boolean {
    console.log('Check Normal Role');
    console.log(this.roles);

    return this.roles.includes('NORMAL');
  }

  public isSuperAdminAccount(): boolean {
    console.log('Check Super Admin Role');

    return this.roles.includes('SUPER_ADMIN');
  }

  //Google Login Process
  public getGoogleLoginLink(): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.authPath + '/google/link';
    return this.httpClient.get(`${requestPath}`, { observe: 'response' });
  }

  //Google Login Handler
  public processGoogleLogin(code: string): Observable<HttpResponse<any>> {
    const params = { token: code };
    const requestPath = this.host + this.authPath + '/google/callback';

    return this.httpClient.post(
      `${requestPath}`,
      {},
      {
        observe: 'response',
        params: params,
      }
    );
  }
}
