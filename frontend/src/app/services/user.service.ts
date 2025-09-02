import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../interfaces';
import { UserPurchase } from '../interfaces/user/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/users`);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/users/${id}`);
  }

  updateUser(id: number, userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/users/${id}`, userData);
  }

  updateUserStatus(userId: number, isActive: boolean): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/users/${userId}/status`, { isActive });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/users/${userId}`);
  }

  getUserPurchases(userId: number): Observable<UserPurchase[]> {
    return this.http.get<UserPurchase[]>(`${this.API_URL}/users/${userId}/purchases`);
  }
}
