package guzman.SalesDashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDTO {
    private Long id;
    private Long invoiceId;
    private Long productId;
    private Integer quantity;
    private Double price;
}
