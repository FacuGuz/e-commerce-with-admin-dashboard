import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

export type AlertType = 'success' | 'error' | 'warning' | 'info';

@Component({
  selector: 'app-alert',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="show" 
         class="text-white px-6 py-4 border-0 rounded relative mb-4"
         [ngClass]="alertClasses">
      <span class="text-xl inline-block mr-5 align-middle">
        <i [class]="iconClass"></i>
      </span>
      <span class="inline-block align-middle mr-8">
        <b class="capitalize">{{ type }}!</b> {{ message }}
      </span>
      <button 
        *ngIf="dismissible"
        class="absolute bg-transparent text-2xl font-semibold leading-none right-0 top-0 mt-4 mr-6 outline-none focus:outline-none"
        (click)="onDismiss()">
        <span>×</span>
      </button>
    </div>
  `,
  styles: []
})
export class AlertComponent {
  @Input() type: AlertType = 'info';
  @Input() message: string = '';
  @Input() show: boolean = true;
  @Input() dismissible: boolean = true;
  @Output() dismiss = new EventEmitter<void>();

  get alertClasses(): string {
    const baseClasses = 'text-white px-6 py-4 border-0 rounded relative mb-4';
    const typeClasses = {
      'success': 'bg-emerald-500',
      'error': 'bg-red-500',
      'warning': 'bg-orange-500',
      'info': 'bg-blue-500'
    };
    return `${baseClasses} ${typeClasses[this.type]}`;
  }

  get iconClass(): string {
    const iconClasses = {
      'success': 'fas fa-check-circle',
      'error': 'fas fa-exclamation-circle',
      'warning': 'fas fa-exclamation-triangle',
      'info': 'fas fa-info-circle'
    };
    return iconClasses[this.type];
  }

  onDismiss(): void {
    this.show = false;
    this.dismiss.emit();
  }
}
