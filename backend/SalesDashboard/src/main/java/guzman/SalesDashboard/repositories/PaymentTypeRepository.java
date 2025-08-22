package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Long> {

    Optional<List<PaymentTypeEntity>> findByName(String paymentTypeName);

}
