import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LayoutService } from '../../../../core/service/layout.service';
import { UserDTO } from '../../../../core/models/SearchResponse';

@Component({
  selector: 'app-search-stuff',
  standalone: false,
  templateUrl: './search-stuff.component.html',
  styleUrl: './search-stuff.component.css'
})
export class SearchStuffComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private ls = inject(LayoutService);

  queriedUsers: UserDTO[] = [];
  isEmpty: boolean = false;
  searchQuery: string = '';

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['query'];
      if (this.searchQuery) {
        this.searchUsers();
      }
    });
  }

  searchUsers() {
    
    this.ls.getQueriedSearch(this.searchQuery).subscribe({
      next: (response) => {
        this.queriedUsers = response;
        this.isEmpty = this.queriedUsers.length === 0;
      },
      error: (error) => {
        console.error('Search error:', error);
        this.isEmpty = true;
      },
      complete: () => {
        console.log('SEARCH DONE');
      }
    });
  }
}