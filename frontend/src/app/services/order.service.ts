import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, CreateOrderRequest, OrderSummary } from '../models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private readonly API_URL = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) { }

  createOrder(orderRequest: CreateOrderRequest): Observable<Order> {
    return this.http.post<Order>(this.API_URL, orderRequest);
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.API_URL);
  }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.API_URL}/${id}`);
  }

  getUserOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.API_URL}/my-orders`);
  }

  updateOrderStatus(id: number, status: string): Observable<Order> {
    return this.http.patch<Order>(`${this.API_URL}/${id}/status`, { status });
  }

  getOrderSummary(): Observable<OrderSummary[]> {
    return this.http.get<OrderSummary[]>(`${this.API_URL}/summary`);
  }
}
