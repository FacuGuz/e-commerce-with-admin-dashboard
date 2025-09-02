import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NotificationsComponent } from './components/shared/notifications.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, NotificationsComponent],
  template: `
    <app-navbar></app-navbar>
    <router-outlet></router-outlet>
    <app-notifications></app-notifications>
  `,
  styles: []
})
export class AppComponent {
  title = 'TiendaOnline';
}
