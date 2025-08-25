import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Cart, CartItem, CartAddItemRequest, CartUpdateItemRequest, CartRemoveItemRequest, CartSummary } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private readonly API_URL = 'http://localhost:8080/api/cart';
  private cartSubject = new BehaviorSubject<Cart | null>(null);
  private cartSummarySubject = new BehaviorSubject<CartSummary>({
    totalItems: 0,
    totalAmount: 0,
    itemCount: 0
  });

  public cart$ = this.cartSubject.asObservable();
  public cartSummary$ = this.cartSummarySubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadCart();
  }

  private loadCart(): void {
    this.getCart().subscribe();
  }

  getCart(): Observable<Cart> {
    return this.http.get<Cart>(this.API_URL).pipe(
      tap(cart => {
        this.cartSubject.next(cart);
        this.updateCartSummary(cart);
      })
    );
  }

  addItem(request: CartAddItemRequest): Observable<Cart> {
    return this.http.post<Cart>(`${this.API_URL}/items`, request).pipe(
      tap(cart => {
        this.cartSubject.next(cart);
        this.updateCartSummary(cart);
      })
    );
  }

  updateItem(request: CartUpdateItemRequest): Observable<Cart> {
    return this.http.put<Cart>(`${this.API_URL}/items/${request.itemId}`, request).pipe(
      tap(cart => {
        this.cartSubject.next(cart);
        this.updateCartSummary(cart);
      })
    );
  }

  removeItem(request: CartRemoveItemRequest): Observable<Cart> {
    return this.http.delete<Cart>(`${this.API_URL}/items/${request.itemId}`).pipe(
      tap(cart => {
        this.cartSubject.next(cart);
        this.updateCartSummary(cart);
      })
    );
  }

  clearCart(): Observable<void> {
    return this.http.delete<void>(this.API_URL).pipe(
      tap(() => {
        this.cartSubject.next(null);
        this.cartSummarySubject.next({
          totalItems: 0,
          totalAmount: 0,
          itemCount: 0
        });
      })
    );
  }

  getCartItemCount(): number {
    const cart = this.cartSubject.value;
    return cart ? cart.totalItems : 0;
  }

  getCartTotal(): number {
    const cart = this.cartSubject.value;
    return cart ? cart.totalAmount : 0;
  }

  isCartEmpty(): boolean {
    const cart = this.cartSubject.value;
    return !cart || cart.items.length === 0;
  }

  private updateCartSummary(cart: Cart): void {
    const summary: CartSummary = {
      totalItems: cart.totalItems,
      totalAmount: cart.totalAmount,
      itemCount: cart.items.length
    };
    this.cartSummarySubject.next(summary);
  }

  // Local cart methods for guest users
  addItemToLocalCart(product: any, quantity: number = 1): void {
    const currentCart = this.cartSubject.value || { 
      items: [], 
      totalItems: 0, 
      totalAmount: 0, 
      id: 0, 
      userId: 0, 
      createdAt: new Date(), 
      updatedAt: new Date() 
    };
    
    const existingItem = currentCart.items.find(item => item.product.id === product.id);
    
    if (existingItem) {
      existingItem.quantity += quantity;
      existingItem.subtotal = existingItem.quantity * existingItem.price;
    } else {
      const newItem: CartItem = {
        id: Date.now(),
        product: product,
        quantity: quantity,
        price: product.price,
        subtotal: product.price * quantity
      };
      currentCart.items.push(newItem);
    }
    
    this.recalculateLocalCart(currentCart);
  }

  removeItemFromLocalCart(itemId: number): void {
    const currentCart = this.cartSubject.value;
    if (!currentCart) return;
    
    currentCart.items = currentCart.items.filter(item => item.id !== itemId);
    this.recalculateLocalCart(currentCart);
  }

  updateLocalCartItem(itemId: number, quantity: number): void {
    const currentCart = this.cartSubject.value;
    if (!currentCart) return;
    
    const item = currentCart.items.find(item => item.id === itemId);
    if (item) {
      item.quantity = quantity;
      item.subtotal = item.quantity * item.price;
      this.recalculateLocalCart(currentCart);
    }
  }

  private recalculateLocalCart(cart: Cart): void {
    cart.totalItems = cart.items.reduce((sum, item) => sum + item.quantity, 0);
    cart.totalAmount = cart.items.reduce((sum, item) => sum + item.subtotal, 0);
    cart.updatedAt = new Date();
    
    this.cartSubject.next(cart);
    this.updateCartSummary(cart);
  }
}
