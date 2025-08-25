import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { User, UserRole, AuthUser, LoginRequest, RegisterRequest, AuthResponse } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/v1/auth';
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private tokenSubject = new BehaviorSubject<string | null>(null);

  public currentUser$ = this.currentUserSubject.asObservable();
  public token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadStoredAuth();
  }

  private loadStoredAuth(): void {
    const token = localStorage.getItem('auth_token');
    const user = localStorage.getItem('current_user');
    
    if (token && user) {
      this.tokenSubject.next(token);
      this.currentUserSubject.next(JSON.parse(user));
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => {
        this.setAuthFromToken(response.token);
      })
    );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, userData).pipe(
      tap(response => {
        this.setAuthFromToken(response.token);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('current_user');
    localStorage.removeItem('refresh_token');
    this.currentUserSubject.next(null);
    this.tokenSubject.next(null);
  }

  refreshToken(): Observable<{ token: string; refreshToken: string }> {
    const refreshToken = localStorage.getItem('refresh_token');
    return this.http.post<{ token: string; refreshToken: string }>(`${this.API_URL}/refresh`, { refreshToken });
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return this.tokenSubject.value;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === UserRole.ADMIN;
  }

  hasRole(role: UserRole): boolean {
    const user = this.getCurrentUser();
    return user?.role === role;
  }

  private setAuthFromToken(token: string): void {
    // Decodificar el token JWT para obtener información del usuario
    const tokenPayload = this.decodeToken(token);
    
    // Crear un objeto User básico desde el token
    const user: User = {
      id: tokenPayload.sub ? parseInt(tokenPayload.sub) : 0,
      username: tokenPayload.username || tokenPayload.email || '',
      email: tokenPayload.email || '',
      role: tokenPayload.role === 'ROLE_ADMIN' ? UserRole.ADMIN : UserRole.USER,
      isActive: true,
      createdAt: new Date(),
      updatedAt: new Date()
    };

    localStorage.setItem('auth_token', token);
    localStorage.setItem('current_user', JSON.stringify(user));
    
    this.tokenSubject.next(token);
    this.currentUserSubject.next(user);
  }

  private decodeToken(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Error decoding token:', error);
      return {};
    }
  }

  updateProfile(userData: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/profile`, userData).pipe(
      tap(updatedUser => {
        this.currentUserSubject.next(updatedUser);
        localStorage.setItem('current_user', JSON.stringify(updatedUser));
      })
    );
  }

  changePassword(passwordData: { currentPassword: string; newPassword: string }): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/change-password`, passwordData);
  }
}
