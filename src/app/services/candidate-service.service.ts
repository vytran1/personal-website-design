import { Injectable } from '@angular/core';
import { environment } from '../environment/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CandidateServiceService {
  host = environment.apiUrl;
  candidatePath = '/candidates';

  constructor(private httpClient: HttpClient) {}

  public getAllInformation(): Observable<HttpResponse<any>> {
    const requestPath = this.host + this.candidatePath;
    return this.httpClient.get(requestPath, { observe: 'response' });
  }
}
