package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    
    Optional<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);
    
    List<InvoiceEntity> findByUserId(Long userId);
    
    List<InvoiceEntity> findByStatus(InvoiceEntity.InvoiceStatus status);
}
