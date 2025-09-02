export interface User {
  id: number;
  username: string;
  email: string;
  fullname: string;
  phoneNumber?: string;
  address?: string;
  role: UserRole;
  isActive: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN'
}

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  fullname: string;
  phoneNumber?: string;
  address?: string;
  role: UserRole;
  isActive: boolean;
}

export interface AuthUser {
  user: User;
  token: string;
  refreshToken: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullname: string;
  phoneNumber?: string;
  address?: string;
}

// Backend response interface
export interface AuthResponse {
  token: string;
}
