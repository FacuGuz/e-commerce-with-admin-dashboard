package guzman.SalesDashboard.controllers;

import guzman.SalesDashboard.dtos.MercadoPagoPreferenceDTO;
import guzman.SalesDashboard.dtos.MercadoPagoPreferenceResponseDTO;
import guzman.SalesDashboard.implementations.MercadoPagoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PaymentController {

    private final MercadoPagoServiceImpl mercadoPagoService;

    @PostMapping("/mercadopago/preference")
    public ResponseEntity<MercadoPagoPreferenceResponseDTO> createMercadoPagoPreference(@RequestBody MercadoPagoPreferenceDTO request) {
        try {
            System.out.println("=== PAYMENT CONTROLLER ===");
            System.out.println("Creating MercadoPago preference for order: " + request.getOrderId());
            System.out.println("=========================");
            
            MercadoPagoPreferenceResponseDTO preference = mercadoPagoService.createPreference(request);
            
            System.out.println("=== RESPONSE CREATED ===");
            System.out.println("Preference ID: " + preference.getId());
            System.out.println("Init Point: " + preference.getInitPoint());
            System.out.println("Sandbox Init Point: " + preference.getSandboxInitPoint());
            System.out.println("External Reference: " + preference.getExternalReference());
            System.out.println("=========================");
            
            return ResponseEntity.ok(preference);
            
        } catch (Exception e) {
            System.out.println("ERROR in createMercadoPagoPreference: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
