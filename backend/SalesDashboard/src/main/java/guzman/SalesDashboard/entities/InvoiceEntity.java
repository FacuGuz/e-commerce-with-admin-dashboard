package guzman.SalesDashboard.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "invoice")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Column(name = "userId", insertable = false, updatable = false)
    private Long userId;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<InvoiceDetailEntity> invoiceDetails;

    private Double subtotal;
    private Double tax;
    private Double shipping;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Embedded
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "billing_street")),
        @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
        @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip_code")),
        @AttributeOverride(name = "country", column = @Column(name = "billing_country"))
    })
    private Address billingAddress;

    // Cambiado de enum a String para evitar restricciones de BD
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(updatable = false)
    private LocalDateTime invoiceDate;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.invoiceDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = InvoiceStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum InvoiceStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }

    // Constantes para métodos de pago (ahora que es String)
    public static final class PaymentMethods {
        public static final String CREDIT_CARD = "CREDIT_CARD";
        public static final String DEBIT_CARD = "DEBIT_CARD";
        public static final String BANK_TRANSFER = "BANK_TRANSFER";
        public static final String CASH = "CASH";
        public static final String MERCADOPAGO = "MERCADOPAGO";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((subtotal == null) ? 0 : subtotal.hashCode());
        result = prime * result + ((tax == null) ? 0 : tax.hashCode());
        result = prime * result + ((shipping == null) ? 0 : shipping.hashCode());
        result = prime * result + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((shippingAddress == null) ? 0 : shippingAddress.hashCode());
        result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
        result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
        result = prime * result + ((paymentStatus == null) ? 0 : paymentStatus.hashCode());
        result = prime * result + ((invoiceDate == null) ? 0 : invoiceDate.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        // NO incluir invoiceDetails para evitar ciclo infinito
        return result;
    }
}
