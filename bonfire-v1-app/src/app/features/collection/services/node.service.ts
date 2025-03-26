import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../../../core/auth/services/auth.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { BonfireMarkerRequest} from '../models/BonfireMarkerRequest';

@Injectable({
  providedIn: 'root'
})
export class NodeService {

  private authService = inject(AuthService);
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  constructor() { }

  getNodes(username: string): Observable<any>{

    return this.http.get<any>(`${this.apiUrl}/nodes/collections/${username}`)
 
  }

  getBonfires(username: string): Observable<BonfireMarkerRequest> {
    return this.http.get<BonfireMarkerRequest>(`${this.apiUrl}/bonfire/fetch/${username}`);
  }

}
