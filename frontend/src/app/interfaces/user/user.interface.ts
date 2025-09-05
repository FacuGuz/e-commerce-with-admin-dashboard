export interface UserPurchase {
  id: number;
  invoiceNumber: string;
  userId: number;
  status: string;
  items: PurchaseItem[];
  subtotal: number;
  shipping: number;
  totalAmount: number;
  shippingAddress: Address;
  billingAddress: Address;
  paymentMethod: string;
  paymentStatus: string;
  invoiceDate: Date;
  updatedAt: Date;
}

export interface PurchaseItem {
  id: number;
  purchaseId: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
  subtotal: number;
}

export interface Address {
  street: string;
  city: string;
  state: string;
  zipCode: string;
  country: string;
}
