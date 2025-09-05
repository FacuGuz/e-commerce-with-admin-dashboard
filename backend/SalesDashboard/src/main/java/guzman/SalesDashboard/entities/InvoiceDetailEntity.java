package guzman.SalesDashboard.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "invoice_detail")
public class InvoiceDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @Column(name = "invoice_id", insertable = false, updatable = false)
    private Long invoiceId;

    // Campo simple para el ID del producto (sin relación)
    @Column(name = "product_id")
    private Long productId;

    private Integer quantity;
    private Double price;
    private Double subtotal;
    private String productName;

    // Quitamos la relación con ProductEntity para evitar conflictos
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "productId", insertable = false, updatable = false)
    // private ProductEntity product;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((subtotal == null) ? 0 : subtotal.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        // NO incluir invoice ni product para evitar ciclo infinito
        return result;
    }
}
