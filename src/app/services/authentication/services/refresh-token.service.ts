import { Injectable } from '@angular/core';
import { environment } from '../../../environment/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RefreshTokenRequest } from '../model/refresh-token-request.model';

@Injectable({
  providedIn: 'root',
})
export class RefreshTokenService {
  refreshToken: string | null = null;
  host = environment.apiUrl;
  requestPath: string = this.host + '/auth/refresh/token';

  constructor(private httpClient: HttpClient) {}

  getNewToken(username: string): Observable<HttpResponse<any>> {
    if (!this.refreshToken) {
      this.loadRefreshToken();
    }

    const requestBody: RefreshTokenRequest = {
      username: username,
      refreshToken: this.refreshToken ?? '',
    };

    return this.httpClient.post(`${this.requestPath}`, requestBody, {
      observe: 'response',
    });
  }

  saveRefreshToken(refreshToken: string): void {
    localStorage.setItem('refreshToken', refreshToken);
    this.refreshToken = refreshToken;
  }

  loadRefreshToken(): void {
    this.refreshToken = localStorage.getItem('refreshToken');
  }

  removeRefreshToken() {
    localStorage.removeItem('refreshToken');
  }

  getRefreshToken(): string {
    if (this.refreshToken) {
      return this.refreshToken;
    }
    return '';
  }
}
