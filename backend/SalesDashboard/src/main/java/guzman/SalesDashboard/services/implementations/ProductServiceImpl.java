package guzman.SalesDashboard.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import guzman.SalesDashboard.entities.ProductEntity;
import guzman.SalesDashboard.repositories.ProductRepository;
import guzman.SalesDashboard.services.ProductService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{

    private final ProductRepository productRepository;

    @Override
    public void deleteProduct(Long id) {
        
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public ProductEntity getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return product;
    }

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public List<ProductEntity> getProductsByFilter(String name, Long categoryId) {
        
        if (name != null && categoryId != null) {
            return productRepository.findByNameContainingAndCategoryId(name, categoryId);
        }
        else if (name != null) {
            return productRepository.findByNameContaining(name);
        }
        else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId);
        }
        else {
            return productRepository.findAll();
        }
    }


    public Optional<ProductEntity> updateProduct(Long id, ProductEntity updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setImagePath(updatedProduct.getImagePath());
            

            if (updatedProduct.getCategory() != null) {
                existingProduct.setCategory(updatedProduct.getCategory());
            }



            return productRepository.save(existingProduct);
        });

    }
}
    
    
    

