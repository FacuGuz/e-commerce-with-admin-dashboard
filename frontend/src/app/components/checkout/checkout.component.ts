import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { PaymentService } from '../../services/payment.service';
import { AuthService } from '../../services/auth.service';
import { Cart, CartItem, CreateOrderRequest, Address, PaymentMethod, MercadoPagoPreference, Order } from '../../interfaces';

declare var MercadoPago: any;

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
  showMercadoPagoButton = false;
  preferenceId: string | null = null;
  showMercadoPagoBricks = false;

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
      // Informacion basica del cliente
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      
      // Direccion de envio simplificada
      address: ['', Validators.required],
      city: ['', Validators.required],
      zipCode: ['', Validators.required],
      
      // Terminos y condiciones
      acceptTerms: [false, Validators.requiredTrue]
    });
  }

  ngOnInit(): void {
    // Suscribirse a cambios del carrito
    this.cartService.cart$.subscribe(cart => {
      this.cart = cart;
      if (!cart || cart.items.length === 0) {
        this.router.navigate(['/cart']);
        return;
      }
      
      // Pre-llenar formulario con datos del usuario si esta logueado
      if (this.authService.isAuthenticated()) {
        const user = this.authService.getCurrentUser();
        if (user) {
          console.log('Checkout - User data for autocomplete:', user);
          this.checkoutForm.patchValue({
            fullName: user.fullname || '',
            email: user.email || '',
            phone: user.phoneNumber || '',
            // No autocompletar direccion, ciudad y codigo postal
            // para que el usuario los complete manualmente
          });
          console.log('Checkout - Form values after patch:', this.checkoutForm.value);
        }
      }
    });

    // Verificar parametros de retorno de pago
    this.route.queryParams.subscribe(params => {
      if (params['payment_id'] && params['status']) {
        this.handlePaymentReturn(params['payment_id'], params['status']);
      }
    });
  }

  calculateTotals(): { subtotal: number; shipping: number; total: number } {
    if (!this.cart) return { subtotal: 0, shipping: 0, total: 0 };
    
    const subtotal = this.cart.items.reduce((sum, item) => sum + (item.product.price * item.quantity), 0);
    const shipping = subtotal > 50 ? 0 : 10; // Envio gratis sobre $50
    const total = subtotal + shipping;
    
    return { subtotal, shipping, total };
  }

  async proceedToPayment(): Promise<void> {
    if (this.checkoutForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    
    try {
      // Crear orden primero
      const orderData: CreateOrderRequest = {
        items: this.cart!.items.map(item => ({
          productId: item.product.id,
          productName: item.product.name,
          quantity: item.quantity,
          unitPrice: item.product.price,
          subtotal: item.subtotal
        })),
        shippingAddress: {
          street: this.checkoutForm.get('address')?.value,
          city: this.checkoutForm.get('city')?.value,
          state: 'Buenos Aires', // Por defecto para simplificar
          zipCode: this.checkoutForm.get('zipCode')?.value,
          country: 'Argentina'
        },
        billingAddress: {
          street: this.checkoutForm.get('address')?.value,
          city: this.checkoutForm.get('city')?.value,
          state: 'Buenos Aires', // Por defecto para simplificar
          zipCode: this.checkoutForm.get('zipCode')?.value,
          country: 'Argentina'
        },
        paymentMethod: PaymentMethod.MERCADOPAGO
      };

      const isAuthenticated = this.authService.isAuthenticated();
      let order: Order;
      
      if (isAuthenticated) {
        order = await firstValueFrom(this.orderService.createOrder(orderData));
      } else {
        order = await firstValueFrom(this.orderService.createGuestOrder(orderData));
      }
      
      if (!order) {
        throw new Error('Error al crear la orden');
      }

      // Crear preferencia de MercadoPago
      const totals = this.calculateTotals();
      const preferenceData = {
        orderId: order.id,
        items: this.cart!.items.map(item => ({
          id: item.product.id.toString(),
          title: item.product.name,
          quantity: item.quantity,
          unit_price: item.product.price,
          currency_id: 'ARS',
          description: item.product.description || item.product.name,
          picture_url: item.product.imagePath || 'https://via.placeholder.com/150x150?text=Producto'
        })),
        payer: {
          name: this.checkoutForm.get('fullName')?.value,
          email: this.checkoutForm.get('email')?.value
        },
        totalAmount: totals.total
      };

      console.log('Creando preferencia de MercadoPago:', preferenceData);

      const preference = await firstValueFrom(this.paymentService.createMercadoPagoPreference(preferenceData));
      
      if (!preference) {
        throw new Error('Error al crear la preferencia de pago');
      }

      console.log('Preferencia creada:', preference);

      // Mostrar Checkout Bricks de MercadoPago
      this.preferenceId = preference.id;
      this.showMercadoPagoBricks = true;
      
      // Renderizar los Checkout Bricks despues de un pequeño delay para asegurar que el DOM este listo
      setTimeout(() => {
        this.renderMercadoPagoBricks(preference.id);
      }, 100);
      
    } catch (error) {
      console.error('Error durante el checkout:', error);
      alert('Error durante el proceso de checkout. Por favor, intentalo de nuevo.');
    } finally {
      this.isLoading = false;
    }
  }

  private renderMercadoPagoBricks(preferenceId: string): void {
    try {
      if (typeof MercadoPago === 'undefined') {
        console.error('MercadoPago SDK no esta disponible');
        return;
      }

      // Inicializar MercadoPago
      const mp = new MercadoPago(this.paymentService.getMercadoPagoPublicKey());
      
      // Crear el Brick de Wallet (billetera)
      const wallet = mp.bricks().create('wallet', 'mercadopago-checkout', {
        initialization: {
          preferenceId: preferenceId
        },
        callbacks: {
          onReady: () => {
            console.log('Wallet Brick listo');
          },
          onSubmit: (cardFormData: any) => {
            console.log('Formulario enviado:', cardFormData);
          },
          onError: (error: any) => {
            console.error('Error en Wallet Brick:', error);
          }
        }
      });

      // Crear el Brick de Payment (pago con tarjeta)
      const payment = mp.bricks().create('payment', 'mercadopago-checkout', {
        initialization: {
          preferenceId: preferenceId
        },
        callbacks: {
          onReady: () => {
            console.log('Payment Brick listo');
          },
          onSubmit: (cardFormData: any) => {
            console.log('Formulario de pago enviado:', cardFormData);
          },
          onError: (error: any) => {
            console.error('Error en Payment Brick:', error);
          }
        }
      });

      
    } catch (error) {
      console.error('Error al renderizar Checkout Bricks:', error);
    }
  }

  private handlePaymentReturn(paymentId: string, status: string): void {
    if (status === 'approved') {
      // Pago exitoso - redirigir a pagina de exito
      this.router.navigate(['/checkout/success'], { 
        queryParams: { payment_id: paymentId, status: status } 
      });
    } else if (status === 'rejected') {
      // Pago fallido - redirigir a pagina de fallo
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
