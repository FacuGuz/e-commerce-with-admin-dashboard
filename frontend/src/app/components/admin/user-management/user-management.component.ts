import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { User } from '../../../interfaces';
import {UserPurchase} from '../../../interfaces/user/user.interface';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  isLoading = false;
  searchTerm = '';
  selectedUser: User | null = null;
  userPurchases: UserPurchase[] = [];
  showPurchases = false;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.filteredUsers = users;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.isLoading = false;
      }
    });
  }

  onSearch(): void {
    if (!this.searchTerm.trim()) {
      this.filteredUsers = this.users;
    } else {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  onViewPurchases(user: User): void {
    this.selectedUser = user;
    this.showPurchases = true;
    this.loadUserPurchases(user.id);
  }

  loadUserPurchases(userId: number): void {
    this.userService.getUserPurchases(userId).subscribe({
      next: (purchases) => {
        this.userPurchases = purchases;
      },
      error: (error) => {
        console.error('Error loading user purchases:', error);
      }
    });
  }

  onClosePurchases(): void {
    this.showPurchases = false;
    this.selectedUser = null;
    this.userPurchases = [];
  }

  onToggleUserStatus(user: User): void {
    const newStatus = !user.isActive;
    this.userService.updateUserStatus(user.id, newStatus).subscribe({
      next: () => {
        user.isActive = newStatus;
      },
      error: (error) => {
        console.error('Error updating user status:', error);
      }
    });
  }

  onDeleteUser(user: User): void {
    if (confirm(`¿Estás seguro de que quieres eliminar al usuario "${user.username}"?`)) {
      this.userService.deleteUser(user.id).subscribe({
        next: () => {
          this.loadUsers();
        },
        error: (error) => {
          console.error('Error deleting user:', error);
        }
      });
    }
  }
}
