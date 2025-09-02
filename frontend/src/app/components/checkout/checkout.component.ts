import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { PaymentService } from '../../services/payment.service';
import { AuthService } from '../../services/auth.service';
import { Cart, CartItem, CreateOrderRequest, Address, PaymentMethod, MercadoPagoPreference } from '../../interfaces';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class CheckoutComponent implements OnInit {
  cart: Cart | null = null;
  checkoutForm: FormGroup;
  isLoading = false;
  isProcessingPayment = false;
  paymentUrl: string | null = null;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.checkoutForm = this.fb.group({
      // Shipping Address
      shippingStreet: ['', Validators.required],
      shippingCity: ['', Validators.required],
      shippingState: ['', Validators.required],
      shippingZipCode: ['', Validators.required],
      shippingCountry: ['Argentina', Validators.required],
      
      // Billing Address (same as shipping by default)
      billingStreet: ['', Validators.required],
      billingCity: ['', Validators.required],
      billingState: ['', Validators.required],
      billingZipCode: ['', Validators.required],
      billingCountry: ['Argentina', Validators.required],
      
      // Contact Information
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      
      // Payment Method
      paymentMethod: [PaymentMethod.CREDIT_CARD, Validators.required],
      
      // Terms and conditions
      acceptTerms: [false, Validators.requiredTrue]
    });
  }

  ngOnInit(): void {
    // Subscribe to cart changes
    this.cartService.cart$.subscribe(cart => {
      this.cart = cart;
      if (!cart || cart.items.length === 0) {
        this.router.navigate(['/cart']);
        return;
      }
      
      // Pre-fill form with user data if logged in
      if (this.authService.isAuthenticated()) {
        const user = this.authService.getCurrentUser();
        if (user) {
          this.checkoutForm.patchValue({
            email: user.email || ''
            // Note: user.address is a string, not an object with street, city, etc.
            // Address fields will remain empty for user to fill
          });
        }
      }
    });

    // Check for payment success/failure from URL params
    this.route.queryParams.subscribe(params => {
      if (params['payment_id'] && params['status']) {
        this.handlePaymentReturn(params['payment_id'], params['status']);
      }
    });
  }

  copyShippingToBilling(): void {
    const shippingValues = {
      billingStreet: this.checkoutForm.get('shippingStreet')?.value,
      billingCity: this.checkoutForm.get('shippingCity')?.value,
      billingState: this.checkoutForm.get('shippingState')?.value,
      billingZipCode: this.checkoutForm.get('shippingZipCode')?.value,
      billingCountry: this.checkoutForm.get('shippingCountry')?.value
    };
    this.checkoutForm.patchValue(shippingValues);
  }

  calculateTotals(): { subtotal: number; shipping: number; tax: number; total: number } {
    if (!this.cart) return { subtotal: 0, shipping: 0, tax: 0, total: 0 };
    
    const subtotal = this.cart.items.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    const shipping = subtotal > 50 ? 0 : 10; // Free shipping over $50
    const tax = subtotal * 0.21; // 21% tax rate
    const total = subtotal + shipping + tax;
    
    return { subtotal, shipping, tax, total };
  }

  async proceedToPayment(): Promise<void> {
    if (this.checkoutForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    
    try {
      // Create order first
      const orderData: CreateOrderRequest = {
        items: this.cart!.items.map(item => ({
          productId: item.product.id,
          productName: item.product.name,
          quantity: item.quantity,
          unitPrice: item.product.price,
          subtotal: item.subtotal
        })),
        shippingAddress: {
          street: this.checkoutForm.get('shippingStreet')?.value,
          city: this.checkoutForm.get('shippingCity')?.value,
          state: this.checkoutForm.get('shippingState')?.value,
          zipCode: this.checkoutForm.get('shippingZipCode')?.value,
          country: this.checkoutForm.get('shippingCountry')?.value
        },
        billingAddress: {
          street: this.checkoutForm.get('billingStreet')?.value,
          city: this.checkoutForm.get('billingCity')?.value,
          state: this.checkoutForm.get('billingState')?.value,
          zipCode: this.checkoutForm.get('billingZipCode')?.value,
          country: this.checkoutForm.get('billingCountry')?.value
        },
        paymentMethod: this.checkoutForm.get('paymentMethod')?.value.toString()
      };

      // Log para debuggear
      console.log('=== FRONTEND ORDER DATA ===');
      console.log('Order Data:', orderData);
      console.log('Payment Method Value:', this.checkoutForm.get('paymentMethod')?.value);
      console.log('Payment Method Type:', typeof this.checkoutForm.get('paymentMethod')?.value);
      console.log('Cart Items:', this.cart?.items);
      console.log('Form Values:', {
        shippingStreet: this.checkoutForm.get('shippingStreet')?.value,
        shippingCity: this.checkoutForm.get('shippingCity')?.value,
        shippingState: this.checkoutForm.get('shippingState')?.value,
        shippingZipCode: this.checkoutForm.get('shippingZipCode')?.value,
        shippingCountry: this.checkoutForm.get('shippingCountry')?.value,
        billingStreet: this.checkoutForm.get('billingStreet')?.value,
        billingCity: this.checkoutForm.get('billingCity')?.value,
        billingState: this.checkoutForm.get('billingState')?.value,
        billingZipCode: this.checkoutForm.get('billingZipCode')?.value,
        billingCountry: this.checkoutForm.get('billingCountry')?.value,
        paymentMethod: this.checkoutForm.get('paymentMethod')?.value
      });
      console.log('==========================');

      const order = await firstValueFrom(this.orderService.createOrder(orderData));
      
      if (!order) {
        throw new Error('Failed to create order');
      }

      // Create MercadoPago preference
      const totals = this.calculateTotals();
      const preferenceData = {
        orderId: order.id,
        items: this.cart!.items.map(item => ({
          id: item.product.id.toString(),
          title: item.product.name,
          quantity: item.quantity,
          unit_price: item.product.price,
          currency_id: 'ARS',
          description: item.product.description,
          picture_url: item.product.imagePath
        })),
        payer: {
          name: this.authService.getCurrentUser()?.fullname || 'Cliente',
          email: this.checkoutForm.get('email')?.value
        },
        totalAmount: totals.total
      };

      const preference = await firstValueFrom(this.paymentService.createMercadoPagoPreference(preferenceData));
      
      if (!preference) {
        throw new Error('Failed to create payment preference');
      }

      // Log para debuggear la respuesta de MercadoPago
      console.log('=== MERCADOPAGO PREFERENCE RESPONSE ===');
      console.log('Preference:', preference);
      console.log('Init Point:', preference.initPoint);
      console.log('Sandbox Init Point:', preference.sandboxInitPoint);
      console.log('=====================================');

      // Redirect to MercadoPago (ahora es una URL simulada)
      this.paymentUrl = preference.initPoint;
      console.log('Redirecting to:', this.paymentUrl);
      window.location.href = this.paymentUrl;
      
    } catch (error) {
      console.error('Error during checkout:', error);
      alert('Error durante el proceso de checkout. Por favor, inténtalo de nuevo.');
    } finally {
      this.isLoading = false;
    }
  }

  private handlePaymentReturn(paymentId: string, status: string): void {
    if (status === 'approved') {
      // Payment successful - redirect to success page
      this.router.navigate(['/checkout/success'], { 
        queryParams: { payment_id: paymentId, status: status } 
      });
    } else if (status === 'rejected') {
      // Payment failed - redirect to failure page
      this.router.navigate(['/checkout/failure'], { 
        queryParams: { payment_id: paymentId, status: status } 
      });
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.checkoutForm.controls).forEach(key => {
      const control = this.checkoutForm.get(key);
      control?.markAsTouched();
    });
  }

  goBackToCart(): void {
    this.router.navigate(['/cart']);
  }
}
