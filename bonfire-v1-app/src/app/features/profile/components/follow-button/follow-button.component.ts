import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { SocialService } from '../../services/social.service';
import { FollowRequest } from '../../models/FollowRequest';

@Component({
  selector: 'app-follow-button',
  standalone: false,
  templateUrl: './follow-button.component.html',
  styleUrl: './follow-button.component.css'
})
export class FollowButtonComponent implements OnInit{
  
  private ss = inject(SocialService);

  @Input()
  userPointer!: string;
  
  @Output()
  followStatusChanged = new EventEmitter<boolean>();

  isFollowing!: boolean;

  ngOnInit(): void {

    this.isFollowingFetch();
      
  }

  follow(){
    const followRequest: FollowRequest = {
      following: this.userPointer
    }
    this.ss.followUser(followRequest).subscribe({
      next: (response) => {console.log('Follow: ', response)},
      error: (error) => {console.log(error)},
      complete: () => {console.log("Follow User Action")}
    })

    this.isFollowing = true;
    this.followStatusChanged.emit(true);

  }

  unfollow(){
    const followRequest: FollowRequest = {
      following: this.userPointer
    }
    this.ss.unfollowUser(followRequest).subscribe({
      next: (response) => {console.log('unFollow: ', response)},
      error: (error) => {console.log(error)},
      complete: () => {console.log("unFollow User Action")}
    })

    this.isFollowing = false;
    this.followStatusChanged.emit(true);

  }

  isFollowingFetch(){
    this.ss.isFollowing(this.userPointer).subscribe({
      next: (response) => {this.isFollowing = response, console.log('IsFollowingFetch: ', response)},
      error: (error) => console.log(error),
      complete: () => console.log("Completed IsFollowingCheck()")
    })
    


  }



}
