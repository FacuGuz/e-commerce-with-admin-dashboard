import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardData, DashboardMetrics } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly API_URL = 'http://localhost:8080/api/admin/dashboard';

  constructor(private http: HttpClient) { }

  getDashboardData(period: string = 'month'): Observable<DashboardData> {
    return this.http.get<DashboardData>(`${this.API_URL}`, {
      params: { period }
    });
  }

  getMetrics(period: string = 'month'): Observable<DashboardMetrics> {
    return this.http.get<DashboardMetrics>(`${this.API_URL}/metrics`, {
      params: { period }
    });
  }

  getSalesChart(period: string = 'month', groupBy: string = 'day'): Observable<any> {
    return this.http.get(`${this.API_URL}/sales-chart`, {
      params: { period, groupBy }
    });
  }

  getTopProducts(limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/top-products`, {
      params: { limit: limit.toString() }
    });
  }

  getTopCategories(limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/top-categories`, {
      params: { limit: limit.toString() }
    });
  }

  getRecentOrders(limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/recent-orders`, {
      params: { limit: limit.toString() }
    });
  }

  getRevenueData(period: string = 'month'): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/revenue`, {
      params: { period }
    });
  }

  getCustomerStats(): Observable<{
    totalCustomers: number;
    newCustomers: number;
    activeCustomers: number;
    customerGrowth: number;
  }> {
    return this.http.get<{
      totalCustomers: number;
      newCustomers: number;
      activeCustomers: number;
      customerGrowth: number;
    }>(`${this.API_URL}/customer-stats`);
  }

  getInventoryStats(): Observable<{
    totalProducts: number;
    lowStockProducts: number;
    outOfStockProducts: number;
    totalValue: number;
  }> {
    return this.http.get<{
      totalProducts: number;
      lowStockProducts: number;
      outOfStockProducts: number;
      totalValue: number;
    }>(`${this.API_URL}/inventory-stats`);
  }

  exportDashboardReport(period: string = 'month', format: 'pdf' | 'excel'): Observable<Blob> {
    return this.http.get(`${this.API_URL}/export`, {
      params: { period, format },
      responseType: 'blob'
    });
  }
}
