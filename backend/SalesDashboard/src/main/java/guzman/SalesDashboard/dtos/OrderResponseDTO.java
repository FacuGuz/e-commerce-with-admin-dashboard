package guzman.SalesDashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    
    private Long id;
    private String invoiceNumber;
    private Long userId;
    private String status;
    private List<InvoiceItemResponseDTO> items;
    private Double subtotal;
    private Double tax;
    private Double shipping;
    private Double totalAmount;
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime invoiceDate;
    private LocalDateTime updatedAt;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InvoiceItemResponseDTO {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
        private Double subtotal;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressDTO {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }
}
