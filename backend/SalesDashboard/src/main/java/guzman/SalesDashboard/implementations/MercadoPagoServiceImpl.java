package guzman.SalesDashboard.implementations;

import guzman.SalesDashboard.dtos.MercadoPagoPreferenceDTO;
import guzman.SalesDashboard.dtos.MercadoPagoPreferenceResponseDTO;
import guzman.SalesDashboard.services.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MercadoPagoServiceImpl implements MercadoPagoService {

    @Override
    public MercadoPagoPreferenceResponseDTO createPreference(MercadoPagoPreferenceDTO request) {
        // Por ahora retornamos una respuesta simulada para testing
        // En producción aquí iría la integración real con MercadoPago API
        
        System.out.println("=== CREATING MERCADOPAGO PREFERENCE ===");
        System.out.println("Order ID: " + request.getOrderId());
        System.out.println("Total Amount: " + request.getTotalAmount());
        System.out.println("Payer: " + request.getPayer().getName() + " (" + request.getPayer().getEmail() + ")");
        System.out.println("Items count: " + request.getItems().size());
        System.out.println("=====================================");
        
        MercadoPagoPreferenceResponseDTO response = new MercadoPagoPreferenceResponseDTO();
        response.setId("test_preference_" + System.currentTimeMillis());
        response.setInitPoint("http://localhost:4200/checkout/success?pref_id=test_preference&order_id=" + request.getOrderId());
        response.setSandboxInitPoint("http://localhost:4200/checkout/success?pref_id=test_preference&order_id=" + request.getOrderId());
        response.setExternalReference(request.getOrderId().toString());
        response.setDateCreated(java.time.LocalDateTime.now().toString());
        response.setLastUpdated(java.time.LocalDateTime.now().toString());
        
        return response;
    }
}
