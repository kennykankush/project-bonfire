import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../core/auth/services/auth.service';
import { ProfileService } from '../../services/profile.service';
import { SocialService } from '../../services/social.service';

@Component({
  selector: 'app-profile-page',
  standalone: false,
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent implements OnInit {
  
  isOwnUser: boolean = false;
  username: string = '';
  profile: any;
  following: number = 0;
  followers: number = 0;
  activeTab: string = 'collection';

  private routeParam = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);
  private profileService = inject(ProfileService);
  private socialService = inject(SocialService);

  ngOnInit(): void {
    this.routeParam.paramMap.subscribe(params => {
      // user clicks on others profile
      const usernameParam = params.get('username');

      // user clicks on Profile
      if (!usernameParam) {
        const currentUser = this.authService.getUsername();
        if (currentUser) {
          this.username = currentUser;
          this.isOwnUser = true;
        } else {
          console.log('No username available');
          this.router.navigate(['/login']);
          return;
        }
      } else {
        this.username = usernameParam;
        this.isOwnUser = this.username === this.authService.getUsername();
      }
      
      this.fetchProfileHeader();
      this.fetchUserStats();
      console.log(`Currently on: ${this.username}'s profile"`);
    });
  }

  fetchProfileHeader(): void {
    this.profileService.getProfileHeader(this.username).subscribe({
      next: (response) => {
        this.profile = response;
        console.log("Fetch Profile Header", response);
      },
      error: (error) => {
        console.warn(error);
      },
      complete: () => {
        console.log('Profile Header Fetch');
      }
    });
  }

  fetchUserStats(): void {
    this.socialService.getUserStats(this.username).subscribe({
      next: (response) => {
        this.following = response.following;
        this.followers = response.followers;
        console.log("Fetch User Stats:", response);
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log("Fetch User Stats Completed");
      }
    });
  }

  onFollowStatusChanged(isFollowing: boolean) {
    console.log("Follow status changed, refreshing stats");
    setTimeout(() => {
      this.fetchUserStats();
    }, 100);
  }
}