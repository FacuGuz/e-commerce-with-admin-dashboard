import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
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

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe(cart => {
      if (cart) {
        this.cartItems = cart.items;
        this.total = cart.totalAmount;
      }
    });
  }

  updateQuantity(item: CartItem, newQuantity: number): void {
    if (newQuantity > 0) {
      this.cartService.updateLocalCartItem(item.id, newQuantity);
    }
  }

  removeItem(item: CartItem): void {
    this.cartService.removeItemFromLocalCart(item.id);
  }

  clearCart(): void {
    this.cartService.clearCart().subscribe();
  }
}
