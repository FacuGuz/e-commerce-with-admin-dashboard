import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { User } from '../../interfaces';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  standalone: true,
  imports: [CommonModule, RouterLink]
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  userRole = '';

  constructor(public authService: AuthService) {
    // Check authentication status on init
    this.isLoggedIn = this.authService.isAuthenticated();
    
    // Subscribe to user changes
    this.authService.currentUser$.subscribe(
      (user: any) => {
        this.userRole = user?.role || '';
        this.isLoggedIn = this.authService.isAuthenticated();
      }
    );
  }

  ngOnInit(): void {
    // Additional initialization if needed
  }
}
