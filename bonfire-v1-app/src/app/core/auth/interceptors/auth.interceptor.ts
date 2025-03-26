import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router)
  const authService = inject(AuthService)
  const token = localStorage.getItem('token');

  const isApiRequest = req.url.includes(environment.apiUrl) || req.url.startsWith('/api');

  if (token && isApiRequest && authService.isLoggedIn()) {
    console.log('Intercepted!');
    const authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}`}
      // headers: req.headers.append('Authorization', `Bearer ${token}`)
    });
  

  return next(authReq).pipe(
    catchError(error => {
      if (error.status === 403){
        console.warn(error);
        localStorage.removeItem('token');
        localStorage.removeItem('expiresIn');
        router.navigate(['/login']);
      } 
      throw error;
      })
    );
  }
  
  return next(req);
};
