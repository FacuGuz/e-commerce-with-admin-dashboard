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
  externalReference: string;
  clientId?: string;
  collectorId?: string;
  notificationUrl?: string;
  expires?: string;
  expirationDateFrom?: string;
  expirationDateTo?: string;
  dateCreated: string;
  lastUpdated: string;
  siteId?: string;
  sponsorId?: string;
  marketplace?: string;
  marketplaceFee?: string;
  differentialPricing?: string;
  applicationId?: string;
  processingModes?: string;
  additionalInfo?: string;
  autoReturn?: string;
  backUrls?: string;
  binaryMode?: string;
  categoryId?: string;
  collector?: string;
  currency?: string;
  items?: string;
  payer?: string;
  paymentMethods?: string;
  shipments?: string;
  statementDescriptor?: string;
}

export interface PaymentStatus {
  id: string;
  status: string;
  statusDetail: string;
  transactionAmount: number;
  transactionId: string;
  lastUpdated: Date;
}
