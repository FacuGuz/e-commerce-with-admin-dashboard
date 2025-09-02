package guzman.SalesDashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoPreferenceResponseDTO {
    
    private String id;
    private String initPoint;
    private String sandboxInitPoint;
    private String externalReference;
    private String clientId;
    private String collectorId;
    private String notificationUrl;
    private String expires;
    private String expirationDateFrom;
    private String expirationDateTo;
    private String dateCreated;
    private String lastUpdated;
    private String siteId;
    private String sponsorId;
    private String marketplace;
    private String marketplaceFee;
    private String differentialPricing;
    private String applicationId;
    private String processingModes;
    private String additionalInfo;
    private String autoReturn;
    private String backUrls;
    private String binaryMode;
    private String categoryId;
    private String collector;
    private String currency;
    private String items;
    private String payer;
    private String paymentMethods;
    private String shipments;
    private String statementDescriptor;
}
