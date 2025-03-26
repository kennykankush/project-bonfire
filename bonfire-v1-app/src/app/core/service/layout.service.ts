import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  constructor() { }

  getPFP(username: string | null): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/users/getPFP/${username}`);
    }

  getQueriedSearch(query: String): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/users/search?query=${query}`)
  }
  
}
