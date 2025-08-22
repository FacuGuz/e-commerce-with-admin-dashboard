import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  features = [
    {
      title: 'Gestión de Productos',
      description: 'Administra tu catálogo de productos de manera eficiente',
      icon: '📦'
    },
    {
      title: 'Análisis de Ventas',
      description: 'Obtén insights detallados sobre el rendimiento de tus ventas',
      icon: '📊'
    },
    {
      title: 'Gestión de Pedidos',
      description: 'Controla el flujo de pedidos desde la recepción hasta la entrega',
      icon: '🛒'
    },
    {
      title: 'Reportes en Tiempo Real',
      description: 'Accede a métricas actualizadas al momento',
      icon: '⚡'
    }
  ];

  stats = [
    { value: '1000+', label: 'Productos Gestionados' },
    { value: '500+', label: 'Clientes Satisfechos' },
    { value: '99%', label: 'Tiempo de Disponibilidad' },
    { value: '24/7', label: 'Soporte Técnico' }
  ];
}
