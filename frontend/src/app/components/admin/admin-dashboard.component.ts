import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { User, UserRole } from '../../interfaces';
import { ProductManagementComponent } from './product-management/product-management.component';
import { CategoryManagementComponent } from './category-management/category-management.component';
import { UserManagementComponent } from './user-management/user-management.component';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  standalone: true,
  imports: [
    CommonModule,
    ProductManagementComponent,
    CategoryManagementComponent,
    UserManagementComponent
  ]
})
export class AdminDashboardComponent implements OnInit {
  currentSection: 'products' | 'categories' | 'users' = 'products';
  currentUser: User | null = null;

  constructor(public authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    // El guard ya verifica la autenticación y el rol de admin
  }

  setSection(section: 'products' | 'categories' | 'users'): void {
    this.currentSection = section;
  }

  isActiveSection(section: string): boolean {
    return this.currentSection === section;
  }


}
