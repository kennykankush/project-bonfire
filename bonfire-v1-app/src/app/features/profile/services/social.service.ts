import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { FollowRequest } from '../models/FollowRequest';

@Injectable({
  providedIn: 'root'
})
export class SocialService {

  constructor(private http: HttpClient) { }

  private apiUrl = environment.apiUrl;

  followUser(followRequest: FollowRequest): Observable<any>{
    return this.http.post<FollowRequest>(`${this.apiUrl}/social/follow`, followRequest);
  }

  unfollowUser(followRequest: FollowRequest): Observable<any>{
    return this.http.delete<FollowRequest>(`${this.apiUrl}/social/unfollow`, {
      body: followRequest
    });
  }

  isFollowing(username: String): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/social/isfollowing/${username}`);
  }

  getUserStats(username: String): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/social/stats/${username}`);
  }





  


}
