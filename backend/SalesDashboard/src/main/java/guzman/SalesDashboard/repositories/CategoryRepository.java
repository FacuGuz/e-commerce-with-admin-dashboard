package guzman.SalesDashboard.repositories;

import guzman.SalesDashboard.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<List<CategoryEntity>> findByName(String categoryName);
    Optional<CategoryEntity> findById(Long id);
}
