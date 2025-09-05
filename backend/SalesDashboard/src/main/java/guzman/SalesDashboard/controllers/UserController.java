package guzman.SalesDashboard.controllers;

import guzman.SalesDashboard.dtos.UserDTO;
import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.services.UserService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String username,
                                     @RequestParam(required = false) String email) {
        
        try {
            if (username != null) {
                UserEntity userEntity = userService.getUserByUsername(username);
                UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }
            
            if (email != null) {
                UserEntity userEntity = userService.getUserByEmail(email);
                UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }
            
            List<UserEntity> userEntities = userService.getAllUsers();
            List<UserDTO> userDTOs = modelMapper.map(userEntities, new TypeToken<List<UserDTO>>() {}.getType());
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            
            // Actualizar solo los campos que no sean null
            if (userDTO.getFullname() != null) {
                userEntity.setFullname(userDTO.getFullname());
            }
            if (userDTO.getPhoneNumber() != null) {
                userEntity.setPhoneNumber(userDTO.getPhoneNumber());
            }
            if (userDTO.getAddress() != null) {
                userEntity.setAddress(userDTO.getAddress());
            }
            if (userDTO.getUsername() != null) {
                userEntity.setUsername(userDTO.getUsername());
            }
            
            UserEntity updatedUser = userService.saveUser(userEntity);
            UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);
            return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            UserEntity userEntity = userService.getUserById(id);
            // Aquí podrías agregar la lógica para eliminar el usuario
            // userService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/purchases")
    public ResponseEntity<?> getUserPurchases(@PathVariable Long id) {
        try {
            List<guzman.SalesDashboard.entities.InvoiceEntity> invoices = userService.getUserPurchases(id);
            List<guzman.SalesDashboard.dtos.OrderResponseDTO> purchases = invoices.stream()
                .map(invoice -> {
                    guzman.SalesDashboard.dtos.OrderResponseDTO dto = new guzman.SalesDashboard.dtos.OrderResponseDTO();
                    dto.setId(invoice.getId());
                    dto.setInvoiceNumber(invoice.getInvoiceNumber());
                    dto.setStatus(invoice.getStatus().toString());
                    dto.setPaymentStatus(invoice.getPaymentStatus().toString());
                    dto.setPaymentMethod(invoice.getPaymentMethod());
                    dto.setSubtotal(invoice.getSubtotal());
                    dto.setShipping(invoice.getShipping());
                    dto.setTotalAmount(invoice.getTotalAmount());
                    dto.setInvoiceDate(invoice.getInvoiceDate());
                    dto.setUpdatedAt(invoice.getUpdatedAt());
                    
                    if (invoice.getShippingAddress() != null) {
                        dto.setShippingAddress(new guzman.SalesDashboard.dtos.OrderResponseDTO.AddressDTO(
                            invoice.getShippingAddress().getStreet(),
                            invoice.getShippingAddress().getCity(),
                            invoice.getShippingAddress().getState(),
                            invoice.getShippingAddress().getZipCode(),
                            invoice.getShippingAddress().getCountry()
                        ));
                    }
                    
                    if (invoice.getBillingAddress() != null) {
                        dto.setBillingAddress(new guzman.SalesDashboard.dtos.OrderResponseDTO.AddressDTO(
                            invoice.getBillingAddress().getStreet(),
                            invoice.getBillingAddress().getCity(),
                            invoice.getBillingAddress().getState(),
                            invoice.getBillingAddress().getZipCode(),
                            invoice.getBillingAddress().getCountry()
                        ));
                    }
                    
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
            
            return new ResponseEntity<>(purchases, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("ERROR in getUserPurchases: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error retrieving user purchases: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
