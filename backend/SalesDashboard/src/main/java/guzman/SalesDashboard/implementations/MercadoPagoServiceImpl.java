package guzman.SalesDashboard.implementations;

import guzman.SalesDashboard.dtos.MercadoPagoPreferenceDTO;
import guzman.SalesDashboard.dtos.MercadoPagoPreferenceResponseDTO;
import guzman.SalesDashboard.services.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoServiceImpl implements MercadoPagoService {

    @Value("${mercadopago.access.token:TEST-3892168938719343-090315-1150d67d2433d049132526c90780604e-223479530}")
    private String accessToken;

    @Value("${mercadopago.sandbox.enabled:true}")
    private boolean sandboxEnabled;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public MercadoPagoPreferenceResponseDTO createPreference(MercadoPagoPreferenceDTO request) {
        try {
            log.info("Creating MercadoPago preference for order: {}", request.getOrderId());
            
            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);
            
            // Crear payload para la API de MercadoPago
            Map<String, Object> preferenceData = new HashMap<>();
            preferenceData.put("external_reference", request.getOrderId().toString());
            
            // Configurar items
            List<Map<String, Object>> items = request.getItems().stream()
                .map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("title", item.getTitle());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("unit_price", item.getUnit_price());
                    itemMap.put("currency_id", item.getCurrency_id());
                    itemMap.put("description", item.getDescription());
                    if (item.getPicture_url() != null && !item.getPicture_url().isEmpty()) {
                        itemMap.put("picture_url", item.getPicture_url());
                    }
                    return itemMap;
                })
                .toList();
            preferenceData.put("items", items);
            
            // Configurar pagador
            Map<String, Object> payer = new HashMap<>();
            payer.put("name", request.getPayer().getName());
            payer.put("surname", "Usuario");
            payer.put("email", request.getPayer().getEmail());
            
            // Configurar dirección del comprador (requerida para sandbox)
            Map<String, Object> address = new HashMap<>();
            address.put("street_name", "Calle Falsa");
            address.put("street_number", 123);
            address.put("zip_code", "1234");
            payer.put("address", address);
            
            // Configurar teléfono del comprador
            Map<String, Object> phone = new HashMap<>();
            phone.put("area_code", "11");
            phone.put("number", "12345678");
            payer.put("phone", phone);
            
            preferenceData.put("payer", payer);
            
            // Configurar URLs de retorno
            Map<String, String> backUrls = new HashMap<>();
            backUrls.put("success", "https://httpbin.org/status/200");
            backUrls.put("failure", "https://httpbin.org/status/200");
            backUrls.put("pending", "https://httpbin.org/status/200");
            preferenceData.put("back_urls", backUrls);
            
            // Configurar notificaciones
            preferenceData.put("notification_url", "https://httpbin.org/post");
            
            // Configurar external_reference para identificar la orden
            preferenceData.put("external_reference", String.valueOf(request.getOrderId()));
            
            // Configurar configuración adicional para sandbox
            preferenceData.put("expires", true);
            preferenceData.put("expiration_date_to", "2025-12-31T23:59:59.000-03:00");
            
            // Crear HTTP entity
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(preferenceData, headers);
            
            // Llamar a la API de MercadoPago
            String apiUrl = "https://api.mercadopago.com/checkout/preferences";
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                
                if (responseBody != null) {
                    log.info("MercadoPago preference created successfully - ID: {}", responseBody.get("id"));
                
                    // Crear respuesta
                    MercadoPagoPreferenceResponseDTO responseDTO = new MercadoPagoPreferenceResponseDTO();
                    responseDTO.setId((String) responseBody.get("id"));
                    responseDTO.setInitPoint((String) responseBody.get("init_point"));
                    responseDTO.setSandboxInitPoint((String) responseBody.get("sandbox_init_point"));
                    responseDTO.setExternalReference((String) responseBody.get("external_reference"));
                    
                    // Validar campos opcionales antes de convertirlos a String
                    Object dateCreated = responseBody.get("date_created");
                    Object lastUpdated = responseBody.get("last_updated");
                    
                    responseDTO.setDateCreated(dateCreated != null ? dateCreated.toString() : null);
                    responseDTO.setLastUpdated(lastUpdated != null ? lastUpdated.toString() : null);
                    
                    return responseDTO;
                } else {
                    log.error("MercadoPago API returned null response body");
                    throw new RuntimeException("Failed to create MercadoPago preference - null response body");
                }
            } else {
                log.error("MercadoPago API returned non-successful status: {}", response.getStatusCode());
                throw new RuntimeException("Failed to create MercadoPago preference - Status: " + response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Error creating MercadoPago preference: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear la preferencia de MercadoPago: " + e.getMessage());
        }
        
    }
}
