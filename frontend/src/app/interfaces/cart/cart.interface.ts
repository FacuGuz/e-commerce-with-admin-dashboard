import { Product } from '../product/product.interface';

export interface CartItem {
  id: number;
  product: Product;
  quantity: number;
  price: number;
  subtotal: number;
}

export interface Cart {
  id: number;
  userId: number;
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
  createdAt: Date;
  updatedAt: Date;
}

export interface CartAddItemRequest {
  productId: number;
  quantity: number;
}

export interface CartUpdateItemRequest {
  itemId: number;
  quantity: number;
}

export interface CartRemoveItemRequest {
  itemId: number;
}

export interface CartSummary {
  totalItems: number;
  totalAmount: number;
  itemCount: number;
}
