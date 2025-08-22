import { Product } from './product.model';

export interface CartItem {
  product: Product;
  quantity: number;
  subtotal: number;
}

export interface Cart {
  items: CartItem[];
  total: number;
  itemCount: number;
}

export interface CartUpdateRequest {
  productId: number;
  quantity: number;
}
