import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PermissionService {
  host = environment.apiUrl;
  path = '/permissions';

  constructor(private httpClient: HttpClient) {}

  listAllPermissionInTheSystem(): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.path;
    return this.httpClient.get(`${requestPath}`, { observe: 'response' });
  }
}
