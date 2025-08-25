import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentRequest, PaymentResponse, MercadoPagoPreference, PaymentStatus } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private readonly API_URL = 'http://localhost:8080/api/payments';

  constructor(private http: HttpClient) { }

  createPayment(paymentRequest: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(this.API_URL, paymentRequest);
  }

  createMercadoPagoPreference(orderData: {
    orderId: number;
    items: any[];
    payer: { name: string; email: string };
    totalAmount: number;
  }): Observable<MercadoPagoPreference> {
    return this.http.post<MercadoPagoPreference>(`${this.API_URL}/mercadopago/preference`, orderData);
  }

  processMercadoPagoPayment(paymentData: {
    preferenceId: string;
    paymentId: string;
    orderId: number;
  }): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${this.API_URL}/mercadopago/process`, paymentData);
  }

  getPaymentStatus(paymentId: string): Observable<PaymentStatus> {
    return this.http.get<PaymentStatus>(`${this.API_URL}/${paymentId}/status`);
  }

  getPaymentById(paymentId: string): Observable<PaymentResponse> {
    return this.http.get<PaymentResponse>(`${this.API_URL}/${paymentId}`);
  }

  getPaymentByOrderId(orderId: number): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(`${this.API_URL}/order/${orderId}`);
  }

  refundPayment(paymentId: string, amount?: number): Observable<PaymentResponse> {
    const refundData = amount ? { amount } : {};
    return this.http.post<PaymentResponse>(`${this.API_URL}/${paymentId}/refund`, refundData);
  }

  // Admin methods
  getAllPayments(): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(`${this.API_URL}/admin/all`);
  }

  getPaymentStats(): Observable<{
    totalPayments: number;
    successfulPayments: number;
    failedPayments: number;
    pendingPayments: number;
    totalRevenue: number;
    averagePaymentAmount: number;
  }> {
    return this.http.get<{
      totalPayments: number;
      successfulPayments: number;
      failedPayments: number;
      pendingPayments: number;
      totalRevenue: number;
      averagePaymentAmount: number;
    }>(`${this.API_URL}/admin/stats`);
  }

  exportPayments(format: 'csv' | 'excel'): Observable<Blob> {
    return this.http.get(`${this.API_URL}/admin/export`, {
      params: { format },
      responseType: 'blob'
    });
  }
}
