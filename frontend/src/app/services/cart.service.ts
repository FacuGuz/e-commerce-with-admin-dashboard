import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Cart, CartItem, CartUpdateRequest } from '../models/cart.model';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartSubject = new BehaviorSubject<Cart>({
    items: [],
    total: 0,
    itemCount: 0
  });

  public cart$ = this.cartSubject.asObservable();

  constructor() {
    this.loadCartFromStorage();
  }

  private loadCartFromStorage(): void {
    const cartData = localStorage.getItem('cart');
    if (cartData) {
      const cart = JSON.parse(cartData);
      this.cartSubject.next(cart);
    }
  }

  private saveCartToStorage(cart: Cart): void {
    localStorage.setItem('cart', JSON.stringify(cart));
  }

  private calculateCart(cart: Cart): Cart {
    const total = cart.items.reduce((sum, item) => sum + item.subtotal, 0);
    const itemCount = cart.items.reduce((sum, item) => sum + item.quantity, 0);
    return { ...cart, total, itemCount };
  }

  addToCart(product: Product, quantity: number = 1): void {
    const currentCart = this.cartSubject.value;
    const existingItem = currentCart.items.find(item => item.product.id === product.id);

    if (existingItem) {
      existingItem.quantity += quantity;
      existingItem.subtotal = existingItem.quantity * existingItem.product.price;
    } else {
      const newItem: CartItem = {
        product,
        quantity,
        subtotal: product.price * quantity
      };
      currentCart.items.push(newItem);
    }

    const updatedCart = this.calculateCart(currentCart);
    this.cartSubject.next(updatedCart);
    this.saveCartToStorage(updatedCart);
  }

  updateCartItem(productId: number, quantity: number): void {
    const currentCart = this.cartSubject.value;
    const item = currentCart.items.find(item => item.product.id === productId);

    if (item) {
      if (quantity <= 0) {
        this.removeFromCart(productId);
        return;
      }
      item.quantity = quantity;
      item.subtotal = item.quantity * item.product.price;
    }

    const updatedCart = this.calculateCart(currentCart);
    this.cartSubject.next(updatedCart);
    this.saveCartToStorage(updatedCart);
  }

  removeFromCart(productId: number): void {
    const currentCart = this.cartSubject.value;
    currentCart.items = currentCart.items.filter(item => item.product.id !== productId);
    
    const updatedCart = this.calculateCart(currentCart);
    this.cartSubject.next(updatedCart);
    this.saveCartToStorage(updatedCart);
  }

  clearCart(): void {
    const emptyCart: Cart = {
      items: [],
      total: 0,
      itemCount: 0
    };
    this.cartSubject.next(emptyCart);
    this.saveCartToStorage(emptyCart);
  }

  getCart(): Cart {
    return this.cartSubject.value;
  }

  getCartItemCount(): number {
    return this.cartSubject.value.itemCount;
  }

  getCartTotal(): number {
    return this.cartSubject.value.total;
  }
}
