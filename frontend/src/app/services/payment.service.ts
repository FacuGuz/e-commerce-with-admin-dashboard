import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PaymentRequest {
  orderId: number;
  amount: number;
  description: string;
  payerEmail: string;
}

export interface PaymentResponse {
  id: string;
  initPoint: string;
  sandboxInitPoint: string;
  status: string;
}

export interface PaymentStatus {
  id: string;
  status: string;
  externalReference: string;
  paymentMethod: string;
  amount: number;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private readonly API_URL = 'http://localhost:8080/api/payments';

  constructor(private http: HttpClient) { }

  createPayment(paymentRequest: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(`${this.API_URL}/create`, paymentRequest);
  }

  getPaymentStatus(paymentId: string): Observable<PaymentStatus> {
    return this.http.get<PaymentStatus>(`${this.API_URL}/${paymentId}/status`);
  }

  processWebhook(webhookData: any): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/webhook`, webhookData);
  }
}
