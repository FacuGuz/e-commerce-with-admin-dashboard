package guzman.SalesDashboard.services;

import guzman.SalesDashboard.dtos.CreateOrderRequestDTO;
import guzman.SalesDashboard.dtos.OrderResponseDTO;

public interface InvoiceService {
    OrderResponseDTO createOrder(CreateOrderRequestDTO request, Long userId);
}
