package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.CategoryEntity;
import guzman.SalesDashboard.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface ProductRepository  extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByCategory(CategoryEntity category);

    List<ProductEntity> findByNameContainingAndCategoryId(String name, Long categoryId);

    List<ProductEntity> findByNameContaining(String name);

    List<ProductEntity> findByCategoryId(Long categoryId);
}
