package guzman.SalesDashboard.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart() {
        // Por ahora, devolver un carrito vacío
        Map<String, Object> cart = new HashMap<>();
        cart.put("id", 1);
        cart.put("userId", 1);
        cart.put("items", new Object[0]);
        cart.put("totalItems", 0);
        cart.put("totalAmount", 0.0);
        cart.put("createdAt", "2024-01-01T00:00:00");
        cart.put("updatedAt", "2024-01-01T00:00:00");
        
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody Map<String, Object> request) {
        // Por ahora, devolver un carrito con el item agregado
        Map<String, Object> cart = new HashMap<>();
        cart.put("id", 1);
        cart.put("userId", 1);
        cart.put("items", new Object[]{request});
        cart.put("totalItems", 1);
        cart.put("totalAmount", 0.0);
        cart.put("createdAt", "2024-01-01T00:00:00");
        cart.put("updatedAt", "2024-01-01T00:00:00");
        
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable Long itemId, @RequestBody Map<String, Object> request) {
        // Por ahora, devolver el carrito actualizado
        Map<String, Object> cart = new HashMap<>();
        cart.put("id", 1);
        cart.put("userId", 1);
        cart.put("items", new Object[]{request});
        cart.put("totalItems", 1);
        cart.put("totalAmount", 0.0);
        cart.put("createdAt", "2024-01-01T00:00:00");
        cart.put("updatedAt", "2024-01-01T00:00:00");
        
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Map<String, Object>> removeItem(@PathVariable Long itemId) {
        // Por ahora, devolver un carrito vacío
        Map<String, Object> cart = new HashMap<>();
        cart.put("id", 1);
        cart.put("userId", 1);
        cart.put("items", new Object[0]);
        cart.put("totalItems", 0);
        cart.put("totalAmount", 0.0);
        cart.put("createdAt", "2024-01-01T00:00:00");
        cart.put("updatedAt", "2024-01-01T00:00:00");
        
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        return ResponseEntity.ok().build();
    }
}
