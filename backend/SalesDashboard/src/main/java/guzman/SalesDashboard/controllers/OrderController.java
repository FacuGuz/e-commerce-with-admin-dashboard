package guzman.SalesDashboard.controllers;

import guzman.SalesDashboard.dtos.CreateOrderRequestDTO;
import guzman.SalesDashboard.dtos.OrderResponseDTO;
import guzman.SalesDashboard.implementations.InvoiceServiceImpl;
import guzman.SalesDashboard.implementations.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final InvoiceServiceImpl invoiceService;
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO request) {
        try {
            // Log para debuggear
            System.out.println("=== CREATE ORDER REQUEST ===");
            System.out.println("Items: " + request.getItems());
            System.out.println("Shipping Address: " + request.getShippingAddress());
            System.out.println("Billing Address: " + request.getBillingAddress());
            System.out.println("Payment Method: " + request.getPaymentMethod());
            System.out.println("==========================");
            
            // Obtener el usuario autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = getUserIdFromAuthentication(authentication);
            
            if (userId == null) {
                System.out.println("ERROR: User ID is null");
                return ResponseEntity.badRequest().build();
            }
            
            System.out.println("User ID: " + userId);
            
            // Crear la orden usando el servicio de facturas
            OrderResponseDTO order = invoiceService.createOrder(request, userId);
            return ResponseEntity.ok(order);
            
        } catch (Exception e) {
            System.out.println("ERROR in createOrder: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Authentication principal type: " + authentication.getPrincipal().getClass().getName());
            System.out.println("Authentication principal: " + authentication.getPrincipal());
            
            // Si el principal es directamente un UserEntity
            if (authentication.getPrincipal() instanceof guzman.SalesDashboard.entities.UserEntity) {
                guzman.SalesDashboard.entities.UserEntity user = 
                    (guzman.SalesDashboard.entities.UserEntity) authentication.getPrincipal();
                System.out.println("Principal is UserEntity: " + user.getFullname());
                System.out.println("User ID from UserEntity: " + user.getId());
                return user.getId();
            }
            
            // Si el principal es un UserDetails, necesitamos obtener el usuario real
            if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User userDetails = 
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
                System.out.println("UserDetails username: " + userDetails.getUsername());
                
                // Buscar el usuario en la base de datos por email
                String email = userDetails.getUsername();
                Long userId = userService.getUserIdByEmail(email);
                System.out.println("User ID found by email '" + email + "': " + userId);
                return userId;
            }
            
            // Si el principal es directamente el usuario
            if (authentication.getPrincipal() instanceof String) {
                String email = (String) authentication.getPrincipal();
                System.out.println("Principal is String (email): " + email);
                // Buscar el usuario por email y retornar su ID
                Long userId = userService.getUserIdByEmail(email);
                System.out.println("User ID found by email '" + email + "': " + userId);
                return userId;
            }
        }
        
        System.out.println("No se pudo obtener el User ID de la autenticación");
        return null;
    }
}
