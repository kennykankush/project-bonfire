import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  private router = inject(Router);
  private authService = inject(AuthService);
  isUserLog!: boolean
  username!: string | null;

  ngOnInit(): void {
      this.isUser();
  }

  deleteToken() {
    localStorage.removeItem('token');
    localStorage.removeItem('expiresIn');
    // this.router.navigate(["/"]);
    window.location.reload();
  }

  isUser(){
    this.isUserLog = this.authService.isLoggedIn();

    if (this.isUserLog){
      this.username = this.authService.getUsername();
    }
  }



}
