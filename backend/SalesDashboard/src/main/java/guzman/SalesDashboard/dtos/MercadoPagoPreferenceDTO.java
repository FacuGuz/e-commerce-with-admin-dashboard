package guzman.SalesDashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoPreferenceDTO {
    
    private Long orderId;
    private List<MercadoPagoItemDTO> items;
    private MercadoPagoPayerDTO payer;
    private Double totalAmount;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MercadoPagoItemDTO {
        private String id;
        private String title;
        private Integer quantity;
        private Double unit_price;
        private String currency_id;
        private String description;
        private String picture_url;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MercadoPagoPayerDTO {
        private String name;
        private String email;
    }
}
