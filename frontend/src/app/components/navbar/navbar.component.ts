import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
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
  user: User | null = null;
  cartItemCount = 0;
  cartTotal = 0;

  constructor(
    public authService: AuthService,
    private cartService: CartService
  ) {
    // Check authentication status on init
    this.isLoggedIn = this.authService.isAuthenticated();
    
    // Subscribe to user changes
    this.authService.currentUser$.subscribe(
      (user: any) => {
        this.user = user;
        this.userRole = user?.role || '';
        this.isLoggedIn = this.authService.isAuthenticated();
      }
    );

    // Subscribe to cart changes
    this.cartService.cartSummary$.subscribe(summary => {
      this.cartItemCount = summary.totalItems;
      this.cartTotal = summary.totalAmount;
    });
  }

  ngOnInit(): void {
    // Additional initialization if needed
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}
