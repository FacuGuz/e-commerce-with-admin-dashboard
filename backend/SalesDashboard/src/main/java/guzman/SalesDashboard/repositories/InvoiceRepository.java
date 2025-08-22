package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
        Optional<List<InvoiceEntity>> findByUserId(Long userId);
        Optional<List<InvoiceEntity>> findByInvoiceDate(LocalDateTime invoiceDate);

}
