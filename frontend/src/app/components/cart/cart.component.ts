import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { CartItem } from '../../models/cart.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  isLoading = false;
  isProcessingOrder = false;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
    });
  }

  updateQuantity(item: CartItem, change: number): void {
    const newQuantity = item.quantity + change;
    if (newQuantity > 0) {
      this.cartService.updateQuantity(item.product.id, newQuantity);
    } else {
      this.removeItem(item);
    }
  }

  removeItem(item: CartItem): void {
    this.cartService.removeFromCart(item.product.id);
  }

  getTotalPrice(): number {
    return this.cartItems.reduce((total, item) => {
      return total + (item.product.price * item.quantity);
    }, 0);
  }

  getTotalItems(): number {
    return this.cartItems.reduce((total, item) => total + item.quantity, 0);
  }

  checkout(): void {
    if (this.cartItems.length === 0) {
      return;
    }

    this.isProcessingOrder = true;
    
    const orderData = {
      items: this.cartItems.map(item => ({
        productId: item.product.id,
        quantity: item.quantity,
        price: item.product.price
      })),
      total: this.getTotalPrice()
    };

    this.orderService.createOrder(orderData).subscribe({
      next: (response) => {
        this.isProcessingOrder = false;
        this.cartService.clearCart();
        this.router.navigate(['/orders', response.id]);
      },
      error: (error) => {
        this.isProcessingOrder = false;
        console.error('Error creating order:', error);
      }
    });
  }

  continueShopping(): void {
    this.router.navigate(['/catalog']);
  }
}
