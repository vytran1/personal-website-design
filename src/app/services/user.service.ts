import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role } from '../models/role/role.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  host = environment.apiUrl;
  path = '/users';

  constructor(private httpClient: HttpClient) {}

  listAllUserEndpoint(): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path;

    return this.httpClient.get(`${requestPath}`, {
      observe: 'response',
    });
  }

  listAllRoleOfOneUser(userId: number): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path;
    return this.httpClient.get(`${requestPath}/roles/${userId}`, {
      observe: 'response',
    });
  }

  updateRolesOfOneUser(
    userId: number,
    roles: Role[]
  ): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path;
    return this.httpClient.put(`${requestPath}/roles/${userId}`, roles, {
      observe: 'response',
    });
  }
}
