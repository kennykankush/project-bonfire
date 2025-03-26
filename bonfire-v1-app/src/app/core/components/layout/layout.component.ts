import { Component, inject, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { LayoutService } from '../../service/layout.service';
import { AuthService } from '../../auth/services/auth.service';
import { UserDTO } from '../../models/SearchResponse';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-layout',
  standalone: false,
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent implements OnInit {

  private router = inject(Router);
  private authService = inject(AuthService);
  private ls = inject(LayoutService);
  private fb = inject(FormBuilder);

  isUserLog!: boolean
  username!: string | null;
  urlLink!: string;
  isDropdownOpen = false;
  searchForm! : FormGroup;
  
  ngOnInit(): void {
      this.isUserPfp();
      this.createForm();
  }

  deleteToken() {
    localStorage.removeItem('token');
    localStorage.removeItem('expiresIn');
    // this.router.navigate(["/"]);
    window.location.reload();
  }

  isUserPfp() {
    this.isUserLog = this.authService.isLoggedIn();
  
    if (this.isUserLog) {
      this.username = this.authService.getUsername();
      
      if (this.username) {
        this.ls.getPFP(this.username).subscribe({
          next: (response) => {
            this.urlLink = response.url; 
            console.log('>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>pfp fetch',response);
          },
          error: (error) => {
            console.log(error);
            this.urlLink = 'https://cdn.discordapp.com/embed/avatars/4.png'; 
          },
          complete: () => console.log('---COMPLETED')
        });
      } else {
        console.log('Username is null even though user is logged in');
        this.urlLink = 'https://cdn.discordapp.com/embed/avatars/4.png';
      }
    } else {
      this.urlLink = 'https://cdn.discordapp.com/embed/avatars/4.png';
    }
  }

  toggleDropdown() {
  this.isDropdownOpen = !this.isDropdownOpen;
  }

  createForm(){
    this.searchForm = this.fb.group({
      search: ['']
    })
  }

  onSearchSubmit() {
    const query = this.searchForm.get('search')?.value;
    
    if (query && query.trim().length > 0) {
      this.router.navigate(['/search'], { 
        queryParams: { query: query } 
      });
    }
  }

  goToSettings(){
    this.router.navigate(['/settings'])
  }






}
