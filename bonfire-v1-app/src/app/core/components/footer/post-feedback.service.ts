import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostFeedbackService {

  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl

  constructor() { }

  postFeedback(feedback: string): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/feedback/email`, feedback);
  }
}
