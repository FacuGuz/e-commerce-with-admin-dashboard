import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  standalone: true,
  imports: [CommonModule]
})
export class NotificationsComponent {
  constructor(public notificationService: NotificationService) {}

  getNotificationIcon(type: string): string {
    switch (type) {
      case 'success':
        return 'M5 13l4 4L19 7';
      case 'error':
        return 'M6 18L18 6M6 6l12 12';
      case 'warning':
        return 'M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z';
      case 'info':
        return 'M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z';
      default:
        return 'M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z';
    }
  }

  getNotificationClasses(type: string): string {
    const baseClasses = 'fixed top-4 right-4 z-50 max-w-sm w-full bg-white shadow-lg rounded-lg pointer-events-auto ring-1 ring-black ring-opacity-5 overflow-hidden';
    
    switch (type) {
      case 'success':
        return `${baseClasses} border-l-4 border-green-400`;
      case 'error':
        return `${baseClasses} border-l-4 border-red-400`;
      case 'warning':
        return `${baseClasses} border-l-4 border-yellow-400`;
      case 'info':
        return `${baseClasses} border-l-4 border-blue-400`;
      default:
        return `${baseClasses} border-l-4 border-gray-400`;
    }
  }

  getIconClasses(type: string): string {
    const baseClasses = 'flex-shrink-0 w-5 h-5';
    
    switch (type) {
      case 'success':
        return `${baseClasses} text-green-400`;
      case 'error':
        return `${baseClasses} text-red-400`;
      case 'warning':
        return `${baseClasses} text-yellow-400`;
      case 'info':
        return `${baseClasses} text-blue-400`;
      default:
        return `${baseClasses} text-gray-400`;
    }
  }
}
