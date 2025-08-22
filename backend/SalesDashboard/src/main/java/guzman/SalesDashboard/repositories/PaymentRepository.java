package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<List<PaymentEntity>> findByPaymentDate(LocalDateTime paymentDate);

}
