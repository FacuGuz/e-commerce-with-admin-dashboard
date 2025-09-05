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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = getUserIdFromAuthentication(authentication);
            
            if (userId == null) {
                return ResponseEntity.badRequest().build();
            }
            
            OrderResponseDTO order = invoiceService.createOrder(request, userId);
            return ResponseEntity.ok(order);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/guest")
    public ResponseEntity<OrderResponseDTO> createGuestOrder(@RequestBody CreateOrderRequestDTO request) {
        try {
            Long guestUserId = null;
            OrderResponseDTO order = invoiceService.createOrder(request, guestUserId);
            return ResponseEntity.ok(order);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof guzman.SalesDashboard.entities.UserEntity) {
                guzman.SalesDashboard.entities.UserEntity user = 
                    (guzman.SalesDashboard.entities.UserEntity) authentication.getPrincipal();
                return user.getId();
            }
            
            if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User userDetails = 
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
                String email = userDetails.getUsername();
                return userService.getUserIdByEmail(email);
            }
            
            if (authentication.getPrincipal() instanceof String) {
                String email = (String) authentication.getPrincipal();
                return userService.getUserIdByEmail(email);
            }
        }
        
        return null;
    }
}
