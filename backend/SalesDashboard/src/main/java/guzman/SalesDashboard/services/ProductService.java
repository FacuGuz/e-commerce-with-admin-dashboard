package guzman.SalesDashboard.services;

import java.util.List;
import java.util.Optional;

import guzman.SalesDashboard.entities.ProductEntity;

public interface ProductService {
    public void deleteProduct(Long id);
    public ProductEntity getProductById(Long id);
    public ProductEntity saveProduct(ProductEntity product);
    public List<ProductEntity> getProductsByFilter(String name, Long categoryId);
    public Optional<ProductEntity> updateProduct(Long id, ProductEntity updatedProduct);

}
