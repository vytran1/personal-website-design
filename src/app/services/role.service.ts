import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role } from '../models/role/role.model';
import { Permission } from '../models/permission/permission.model';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  host = environment.apiUrl;
  path = '/roles';

  constructor(private httpClient: HttpClient) {}

  listAllRoles(): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path;
    return this.httpClient.get(`${requestPath}`, {
      observe: 'response',
    });
  }

  deleteRole(roleId: number): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path + '/' + roleId;
    return this.httpClient.delete(`${requestPath}`, { observe: 'response' });
  }

  checkUniqueOfName(roleName: string): Observable<boolean> {
    const requestPath = this.host + this.path + '/unique/name/' + roleName;
    return this.httpClient.get<boolean>(`${requestPath}`);
  }

  createNewRole(role: Role): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path + '/create';
    return this.httpClient.post(`${requestPath}`, role, {
      observe: 'response',
    });
  }

  listAllRoleOfOneRole(roleId: number): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path + '/permissions/' + roleId;
    return this.httpClient.get(`${requestPath}`, { observe: 'response' });
  }

  updatePermissionOfOneRole(
    roleId: number,
    permissions: Permission[]
  ): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path + '/permissions/' + roleId;
    return this.httpClient.put(`${requestPath}`, permissions, {
      observe: 'response',
    });
  }
}
