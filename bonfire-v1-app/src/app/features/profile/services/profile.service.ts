import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) { }

  private apiUrl = environment.apiUrl;

  getProfileHeader(username: String): Observable<any>{
  
      return this.http.get<any>(`${this.apiUrl}/profile/${username}`)
   
    }

  getMyProfile(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/users/me`);
  }

  updateProfile(formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/users/update`, formData);
  }


}
