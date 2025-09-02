import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { User } from '../../../interfaces';
import { UserPurchase } from '../../../interfaces/user/user.interface';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  filteredUsers: User[] = [];
  isLoading = false;
  searchTerm = '';
  selectedUser: User | null = null;
  userPurchases: UserPurchase[] = [];
  showPurchases = false;
  showEditModal = false;
  editForm: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.editForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      fullname: ['', [Validators.required, Validators.minLength(2)]],
      phoneNumber: [''],
      address: ['']
    });
  }

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
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.fullname.toLowerCase().includes(this.searchTerm.toLowerCase())
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

  onEditUser(user: User): void {
    this.selectedUser = user;
    this.editForm.patchValue({
      username: user.username,
      email: user.email,
      fullname: user.fullname,
      phoneNumber: user.phoneNumber || '',
      address: user.address || ''
    });
    this.showEditModal = true;
  }

  onSaveUser(): void {
    if (this.editForm.valid && this.selectedUser) {
      const updatedUser = this.editForm.value;
      this.userService.updateUser(this.selectedUser.id, updatedUser).subscribe({
        next: (user) => {
          // Update the user in the list
          const index = this.users.findIndex(u => u.id === user.id);
          if (index !== -1) {
            this.users[index] = user;
            this.filteredUsers = [...this.users];
          }
          this.showEditModal = false;
          this.selectedUser = null;
        },
        error: (error) => {
          console.error('Error updating user:', error);
        }
      });
    }
  }

  onCloseEditModal(): void {
    this.showEditModal = false;
    this.selectedUser = null;
    this.editForm.reset();
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
