package guzman.SalesDashboard.controllers;

import guzman.SalesDashboard.dtos.MercadoPagoPreferenceDTO;
import guzman.SalesDashboard.dtos.MercadoPagoPreferenceResponseDTO;
import guzman.SalesDashboard.implementations.MercadoPagoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final MercadoPagoServiceImpl mercadoPagoService;

    @PostMapping("/mercadopago/preference")
    public ResponseEntity<MercadoPagoPreferenceResponseDTO> createMercadoPagoPreference(@RequestBody MercadoPagoPreferenceDTO request) {
        try {
            log.info("Creating MercadoPago preference for order: {}", request.getOrderId());
            
            // Validar que los campos requeridos no sean null
            if (request.getItems() == null || request.getItems().isEmpty()) {
                log.error("Items list is null or empty for order: {}", request.getOrderId());
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getPayer() == null) {
                log.error("Payer information is null for order: {}", request.getOrderId());
                return ResponseEntity.badRequest().build();
            }
            
            // Logging seguro con validaciones
            log.info("Request details - Items: {}, Total: ${}, Payer: {} ({})", 
                request.getItems().size(), 
                request.getTotalAmount() != null ? request.getTotalAmount() : "N/A", 
                request.getPayer().getName() != null ? request.getPayer().getName() : "N/A", 
                request.getPayer().getEmail() != null ? request.getPayer().getEmail() : "N/A");
            
            MercadoPagoPreferenceResponseDTO preference = mercadoPagoService.createPreference(request);
            
            log.info("MercadoPago preference created successfully - ID: {}, Init Point: {}", 
                preference.getId(), preference.getInitPoint());
            
            return ResponseEntity.ok(preference);
            
        } catch (Exception e) {
            log.error("Error creating MercadoPago preference: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/mercadopago/webhook")
    public ResponseEntity<String> handleMercadoPagoWebhook(@RequestBody Map<String, Object> payload) {
        try {
            log.info("Received MercadoPago webhook: {}", payload);
            
            // Extraer información del webhook
            String type = (String) payload.get("type");
            Object data = payload.get("data");
            
            if ("payment".equals(type) && data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> paymentData = (Map<String, Object>) data;
                String paymentId = (String) paymentData.get("id");
                String status = (String) paymentData.get("status");
                String externalReference = (String) paymentData.get("external_reference");
                
                log.info("Payment webhook - ID: {}, Status: {}, Order: {}", 
                    paymentId, status, externalReference);
                
                // Aquí puedes procesar el pago según el status
                // Por ejemplo, actualizar la orden en tu base de datos
                switch (status) {
                    case "approved":
                        log.info("Payment approved for order: {}", externalReference);
                        // TODO: Actualizar orden como pagada
                        break;
                    case "rejected":
                        log.info("Payment rejected for order: {}", externalReference);
                        // TODO: Actualizar orden como rechazada
                        break;
                    case "pending":
                        log.info("Payment pending for order: {}", externalReference);
                        // TODO: Actualizar orden como pendiente
                        break;
                    default:
                        log.warn("Unknown payment status: {} for order: {}", status, externalReference);
                }
            }
            
            return ResponseEntity.ok("OK");
            
        } catch (Exception e) {
            log.error("Error processing MercadoPago webhook: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error processing webhook");
        }
    }
}
