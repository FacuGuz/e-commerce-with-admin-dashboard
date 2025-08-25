export interface PaymentRequest {
  orderId: number;
  amount: number;
  currency: string;
  description: string;
  paymentMethod: string;
  installments?: number;
  cardToken?: string;
  payerEmail: string;
}

export interface PaymentResponse {
  id: string;
  status: string;
  statusDetail: string;
  transactionAmount: number;
  transactionId: string;
  paymentUrl?: string;
  qrCode?: string;
  createdAt: Date;
}

export interface MercadoPagoPreference {
  id: string;
  initPoint: string;
  sandboxInitPoint: string;
  items: {
    id: string;
    title: string;
    quantity: number;
    unit_price: number;
    currency_id: string;
    description?: string;
    picture_url?: string;
  }[];
  payer: {
    name: string;
    email: string;
  };
  back_urls: {
    success: string;
    failure: string;
    pending: string;
  };
  auto_return: string;
  external_reference: string;
  expires: boolean;
  expiration_date_to: string;
}

export interface PaymentStatus {
  id: string;
  status: string;
  statusDetail: string;
  transactionAmount: number;
  transactionId: string;
  lastUpdated: Date;
}
