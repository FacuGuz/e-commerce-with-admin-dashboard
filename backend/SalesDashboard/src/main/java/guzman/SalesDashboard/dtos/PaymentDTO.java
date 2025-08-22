package guzman.SalesDashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long invoiceId;
    private Long paymentTypeId;
    private Double amount;
    private LocalDateTime paymentDate;
}
