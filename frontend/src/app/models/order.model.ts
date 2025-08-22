import { User } from './user.model';
import { Product } from './product.model';

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export interface Order {
  id: number;
  user: User;
  orderDate: Date;
  status: OrderStatus;
  total: number;
  items: OrderItem[];
  shippingAddress?: string;
  paymentMethod?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface OrderItem {
  id: number;
  product: Product;
  quantity: number;
  order: Order;
  price: number;
  subtotal: number;
}

export interface CreateOrderRequest {
  items: {
    productId: number;
    quantity: number;
  }[];
  shippingAddress?: string;
  paymentMethod?: string;
}

export interface OrderSummary {
  id: number;
  orderDate: Date;
  status: OrderStatus;
  total: number;
  itemCount: number;
}
