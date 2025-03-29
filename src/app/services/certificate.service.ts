import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CertificateService {
  public host = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  public getCertificateByCandidateId(): Observable<HttpResponse<any>> {
    return this.httpClient.get(`${this.host}/certificates`, {
      observe: 'response',
    });
  }
}
