import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-checkout-success',
  templateUrl: './checkout-success.component.html',
  standalone: true,
  imports: [CommonModule, RouterLink]
})
export class CheckoutSuccessComponent implements OnInit {
  paymentId: string | null = null;
  orderNumber: string | null = null;

  constructor(
    private cartService: CartService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get payment details from URL params
    this.route.queryParams.subscribe(params => {
      this.paymentId = params['payment_id'] || null;
      this.orderNumber = params['order_number'] || null;
    });

    // Clear cart after successful payment
    this.cartService.clearCart().subscribe();
  }

  goToCatalog(): void {
    this.router.navigate(['/catalog']);
  }
}
