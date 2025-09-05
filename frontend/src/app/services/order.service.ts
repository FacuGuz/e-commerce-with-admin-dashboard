import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, CreateOrderRequest, OrderFilter } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly API_URL = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) { }

  getOrders(filter?: OrderFilter): Observable<Order[]> {
    let params = new HttpParams();
    
    if (filter) {
      if (filter.status) params = params.set('status', filter.status);
      if (filter.startDate) params = params.set('startDate', filter.startDate.toISOString());
      if (filter.endDate) params = params.set('endDate', filter.endDate.toISOString());
      if (filter.searchTerm) params = params.set('search', filter.searchTerm);
    }

    return this.http.get<Order[]>(this.API_URL, { params });
  }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.API_URL}/${id}`);
  }

  getOrderByNumber(orderNumber: string): Observable<Order> {
    return this.http.get<Order>(`${this.API_URL}/number/${orderNumber}`);
  }

  createOrder(orderData: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.API_URL, orderData);
  }

  createGuestOrder(orderData: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(`${this.API_URL}/guest`, orderData);
  }

  updateOrderStatus(id: number, status: string): Observable<Order> {
    return this.http.patch<Order>(`${this.API_URL}/${id}/status`, { status });
  }

  cancelOrder(id: number): Observable<Order> {
    return this.http.patch<Order>(`${this.API_URL}/${id}/cancel`, {});
  }

  getUserOrders(userId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.API_URL}/user/${userId}`);
  }

  getOrderHistory(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.API_URL}/history`);
  }

  // Admin methods
  getAllOrders(filter?: OrderFilter): Observable<Order[]> {
    let params = new HttpParams();
    
    if (filter) {
      if (filter.status) params = params.set('status', filter.status);
      if (filter.startDate) params = params.set('startDate', filter.startDate.toISOString());
      if (filter.endDate) params = params.set('endDate', filter.endDate.toISOString());
      if (filter.searchTerm) params = params.set('search', filter.searchTerm);
    }

    return this.http.get<Order[]>(`${this.API_URL}/admin/all`, { params });
  }

  getOrderStats(): Observable<{
    totalOrders: number;
    pendingOrders: number;
    completedOrders: number;
    cancelledOrders: number;
    totalRevenue: number;
  }> {
    return this.http.get<{
      totalOrders: number;
      pendingOrders: number;
      completedOrders: number;
      cancelledOrders: number;
      totalRevenue: number;
    }>(`${this.API_URL}/admin/stats`);
  }

  exportOrders(format: 'csv' | 'excel', filter?: OrderFilter): Observable<Blob> {
    let params = new HttpParams().set('format', format);
    
    if (filter) {
      if (filter.status) params = params.set('status', filter.status);
      if (filter.startDate) params = params.set('startDate', filter.startDate.toISOString());
      if (filter.endDate) params = params.set('endDate', filter.endDate.toISOString());
      if (filter.searchTerm) params = params.set('search', filter.searchTerm);
    }

    return this.http.get(`${this.API_URL}/admin/export`, {
      params,
      responseType: 'blob'
    });
  }
}
