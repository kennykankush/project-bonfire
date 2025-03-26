import { Component, inject, OnInit } from '@angular/core';
import { FeedService } from '../../service/feed.service';
import { AuthService } from '../../../../core/auth/services/auth.service';
import { FeedContent } from '../../models/FeedContent';

@Component({
  selector: 'app-user-feed',
  standalone: false,
  templateUrl: './user-feed.component.html',
  styleUrl: './user-feed.component.css'
})
export class UserFeedComponent implements OnInit {

  private feedServe = inject(FeedService);
  private as = inject(AuthService);
  feed!: FeedContent[];

  constructor() {}
  
  ngOnInit(): void {
    this.getFeed();

    

  }

  getFeed(){
    const username = this.as.getUsername();

    this.feedServe.getFeed(username).subscribe(
      {
        next: (response) => {this.feed = response},
        error: (error) => {console.log(error)},
        complete: () => {console.log('FEED FETCHED')}
      })

  }

}
