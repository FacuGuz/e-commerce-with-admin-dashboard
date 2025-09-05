package guzman.SalesDashboard.implementations;

import guzman.SalesDashboard.dtos.CreateOrderRequestDTO;
import guzman.SalesDashboard.dtos.OrderResponseDTO;
import guzman.SalesDashboard.entities.Address;
import guzman.SalesDashboard.entities.InvoiceDetailEntity;
import guzman.SalesDashboard.entities.InvoiceEntity;
import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.repositories.InvoiceRepository;
import guzman.SalesDashboard.repositories.UserRepository;
import guzman.SalesDashboard.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId) {
        // Crear la factura
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setStatus(InvoiceEntity.InvoiceStatus.PENDING);
        invoice.setPaymentStatus(InvoiceEntity.PaymentStatus.PENDING);
        invoice.setPaymentMethod(request.getPaymentMethod()); // Ahora es String, no enum
        
        // Configurar direcciones
        invoice.setShippingAddress(convertToAddress(request.getShippingAddress()));
        invoice.setBillingAddress(convertToAddress(request.getBillingAddress()));
        
        // Para guest users, no asignamos usuario
        if (userId != null) {
            // Buscar el usuario por ID
            UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
            invoice.setUser(user);
        }
        // Si userId es null, la factura se crea sin usuario (guest order)
        
        // Calcular totales
        double subtotal = request.getItems().stream()
                .mapToDouble(item -> item.getSubtotal())
                .sum();
        double shipping = subtotal > 50 ? 0 : 10; // Envío gratis sobre $50
        double total = subtotal + shipping;
        
        invoice.setSubtotal(subtotal);
        invoice.setTax(0.0); // Sin impuestos
        invoice.setShipping(shipping);
        invoice.setTotalAmount(total);
        
        // Crear detalles de la factura
        Set<InvoiceDetailEntity> details = new HashSet<>();
        for (CreateOrderRequestDTO.InvoiceItemRequestDTO itemRequest : request.getItems()) {
            InvoiceDetailEntity detail = new InvoiceDetailEntity();
            detail.setInvoice(invoice);
            detail.setProductId(itemRequest.getProductId());
            detail.setProductName(itemRequest.getProductName());
            detail.setQuantity(itemRequest.getQuantity());
            detail.setPrice(itemRequest.getUnitPrice());
            detail.setSubtotal(itemRequest.getSubtotal());
            details.add(detail);
        }
        
        invoice.setInvoiceDetails(details);
        
        // Guardar la factura
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);
        
        // Retornar respuesta
        return convertToOrderResponse(savedInvoice);
    }

    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis();
    }

    private Address convertToAddress(CreateOrderRequestDTO.AddressDTO addressDTO) {
        Address address = new Address();
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        return address;
    }

    private OrderResponseDTO convertToOrderResponse(InvoiceEntity invoice) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setUserId(invoice.getUserId());
        response.setStatus(invoice.getStatus().name());
        response.setSubtotal(invoice.getSubtotal());
        response.setShipping(invoice.getShipping());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setPaymentMethod(invoice.getPaymentMethod()); // Ahora es String, no enum
        response.setPaymentStatus(invoice.getPaymentStatus().name());
        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setUpdatedAt(invoice.getUpdatedAt());
        
        // Convertir direcciones
        response.setShippingAddress(convertToAddressDTO(invoice.getShippingAddress()));
        response.setBillingAddress(convertToAddressDTO(invoice.getBillingAddress()));
        
        // Convertir items
        List<OrderResponseDTO.InvoiceItemResponseDTO> items = invoice.getInvoiceDetails().stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);
        
        return response;
    }

    private OrderResponseDTO.AddressDTO convertToAddressDTO(Address address) {
        OrderResponseDTO.AddressDTO addressDTO = new OrderResponseDTO.AddressDTO();
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    private OrderResponseDTO.InvoiceItemResponseDTO convertToItemResponse(InvoiceDetailEntity detail) {
        OrderResponseDTO.InvoiceItemResponseDTO itemResponse = new OrderResponseDTO.InvoiceItemResponseDTO();
        itemResponse.setId(detail.getId());
        itemResponse.setProductId(detail.getProductId());
        itemResponse.setProductName(detail.getProductName());
        itemResponse.setQuantity(detail.getQuantity());
        itemResponse.setPrice(detail.getPrice());
        itemResponse.setSubtotal(detail.getSubtotal());
        return itemResponse;
    }
}
