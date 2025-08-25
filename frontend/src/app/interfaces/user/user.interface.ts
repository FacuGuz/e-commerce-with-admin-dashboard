export interface UserPurchase {
  id: number;
  userId: number;
  totalAmount: number;
  status: string;
  createdAt: Date;
  updatedAt: Date;
  items: PurchaseItem[];
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
