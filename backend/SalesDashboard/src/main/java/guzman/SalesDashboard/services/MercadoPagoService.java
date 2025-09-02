package guzman.SalesDashboard.services;

import guzman.SalesDashboard.dtos.MercadoPagoPreferenceDTO;
import guzman.SalesDashboard.dtos.MercadoPagoPreferenceResponseDTO;

public interface MercadoPagoService {
    MercadoPagoPreferenceResponseDTO createPreference(MercadoPagoPreferenceDTO request);
}
