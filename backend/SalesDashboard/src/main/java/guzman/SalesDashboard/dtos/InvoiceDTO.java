package guzman.SalesDashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id;
    private String invoiceNumber;
    private Long userId;
    private Double totalAmount;
    private LocalDateTime invoiceDate;
    private Set<InvoiceDetailDTO> invoiceDetails;
}
