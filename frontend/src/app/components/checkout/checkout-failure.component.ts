import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-checkout-failure',
  templateUrl: './checkout-failure.component.html',
  standalone: true,
  imports: [CommonModule, RouterLink]
})
export class CheckoutFailureComponent implements OnInit {
  paymentId: string | null = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get payment details from URL params
    this.route.queryParams.subscribe(params => {
      this.paymentId = params['payment_id'] || null;
      this.errorMessage = params['error_message'] || 'El pago no pudo ser procesado.';
    });
  }

  retryPayment(): void {
    // Go back to cart to retry
    this.router.navigate(['/cart']);
  }

  goToCatalog(): void {
    this.router.navigate(['/catalog']);
  }
}
