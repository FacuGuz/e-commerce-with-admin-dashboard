package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.InvoiceDetailEntity;
import guzman.SalesDashboard.entities.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, Long> {

}
