import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'bonfire-v1-app';

  showHeader = false;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.router.events.subscribe(() => {
      this.showHeader = this.router.url !== '/' && this.router.url !== '/register' && this.router.url != '/login';
    });
  }

  
}
