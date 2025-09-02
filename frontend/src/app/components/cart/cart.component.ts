import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { CartItem } from '../../interfaces';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  standalone: true,
  imports: [CommonModule]
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  total = 0;
  subtotal = 0;
  shipping = 0;
  taxes = 0;

  constructor(
    private cartService: CartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe(cart => {
      if (cart) {
        this.cartItems = cart.items;
        this.calculateTotals();
      }
    });
  }

  calculateTotals(): void {
    this.subtotal = this.cartItems.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    this.shipping = this.subtotal > 50 ? 0 : 10; // Free shipping over $50
    this.taxes = this.subtotal * 0.21; // 21% tax rate
    this.total = this.subtotal + this.shipping + this.taxes;
  }

  increaseQuantity(item: CartItem): void {
    if (item.quantity < item.product.stock) {
      this.cartService.updateLocalCartItem(item.id, item.quantity + 1);
    }
  }

  decreaseQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      this.cartService.updateLocalCartItem(item.id, item.quantity - 1);
    }
  }

  removeFromCart(item: CartItem): void {
    this.cartService.removeItemFromLocalCart(item.id);
  }

  updateQuantity(item: CartItem, newQuantity: number): void {
    if (newQuantity > 0 && newQuantity <= item.product.stock) {
      this.cartService.updateLocalCartItem(item.id, newQuantity);
    }
  }

  removeItem(item: CartItem): void {
    this.cartService.removeItemFromLocalCart(item.id);
  }

  clearCart(): void {
    this.cartService.clearCart().subscribe();
  }

  proceedToCheckout(): void {
    // Navigate to checkout page
    this.router.navigate(['/checkout']);
  }
}
