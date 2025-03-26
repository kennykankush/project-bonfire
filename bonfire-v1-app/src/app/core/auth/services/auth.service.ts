import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { RegisterRequest } from '../models/RegisterRequest';
import { Observable } from 'rxjs';
import { LoginRequest } from '../models/LoginRequest';
import { environment } from '../../../../environments/environment';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private router = inject(Router);


  private apiUrl = environment.apiUrl
  private hello = environment.hello
  

  constructor(private http: HttpClient) { }

  register(request: RegisterRequest): Observable<any> {
    console.log(this.hello)
    return this.http.post<RegisterRequest>(`${this.apiUrl}/auth/register`, request)
  }

  login(request: LoginRequest): Observable<any> {
    return this.http.post<LoginRequest>(`${this.apiUrl}/auth/login`, request)
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    if (!token) {
      return false;
    }

    const decoded: any = jwtDecode(token);
    const currentTime = Date.now() / 1000;
        
    if (decoded.exp < currentTime) {
    localStorage.removeItem('token');
    localStorage.removeItem('expiresIn');
    return false;
    }
        
    return true;

    }

  getUsername(): string | null {
    const token = localStorage.getItem('token');

    if (!token) {
      return null
    }

    const userName: any = jwtDecode(token);

    return userName.sub

  }

  logOut(){
    localStorage.removeItem('token');
    localStorage.removeItem('expiresIn');
    this.router.navigate(['/login']);
  }
}
