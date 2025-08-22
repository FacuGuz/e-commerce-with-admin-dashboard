import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  isMenuOpen = false;
  isLoggedIn = false;
  userRole = '';

  constructor(private authService: AuthService) {
    this.authService.isAuthenticated$.subscribe(
      isAuth => this.isLoggedIn = isAuth
    );
    this.authService.currentUser$.subscribe(
      user => this.userRole = user?.role || ''
    );
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  logout(): void {
    this.authService.logout();
    this.isMenuOpen = false;
  }

  isAdmin(): boolean {
    return this.userRole === 'ADMIN';
  }
}
