import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  message: string;
  duration?: number;
  show: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  constructor() {}

  showSuccess(message: string, duration: number = 3000): void {
    this.showNotification({
      id: this.generateId(),
      type: 'success',
      message,
      duration,
      show: true
    });
  }

  showError(message: string, duration: number = 5000): void {
    this.showNotification({
      id: this.generateId(),
      type: 'error',
      message,
      duration,
      show: true
    });
  }

  showWarning(message: string, duration: number = 4000): void {
    this.showNotification({
      id: this.generateId(),
      type: 'warning',
      message,
      duration,
      show: true
    });
  }

  showInfo(message: string, duration: number = 3000): void {
    this.showNotification({
      id: this.generateId(),
      type: 'info',
      message,
      duration,
      show: true
    });
  }

  private showNotification(notification: Notification): void {
    const currentNotifications = this.notificationsSubject.value;
    const updatedNotifications = [...currentNotifications, notification];
    
    this.notificationsSubject.next(updatedNotifications);

    // Auto-hide notification after duration
    if (notification.duration) {
      setTimeout(() => {
        this.hideNotification(notification.id);
      }, notification.duration);
    }
  }

  hideNotification(id: string): void {
    const currentNotifications = this.notificationsSubject.value;
    const updatedNotifications = currentNotifications.filter(n => n.id !== id);
    this.notificationsSubject.next(updatedNotifications);
  }

  clearAll(): void {
    this.notificationsSubject.next([]);
  }

  private generateId(): string {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }
}
