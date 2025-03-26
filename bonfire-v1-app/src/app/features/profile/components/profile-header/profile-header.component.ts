import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-profile-header',
  standalone: false,
  templateUrl: './profile-header.component.html',
  styleUrl: './profile-header.component.css'
})
export class ProfileHeaderComponent {
  @Input() isOwnUser: boolean = false;
  @Input() username: string = '';
  @Input() profile: any;
  @Input() following: number = 0;
  @Input() followers: number = 0;
  
  @Output() followStatusChanged = new EventEmitter<boolean>();
  
  onFollowStatusChanged(isFollowing: boolean) {
    console.log("Follow status changed in profile header");
    this.followStatusChanged.emit(isFollowing);
  }
}