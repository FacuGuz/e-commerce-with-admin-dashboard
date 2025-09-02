package guzman.SalesDashboard.controllers;

import java.util.List;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import guzman.SalesDashboard.dtos.CreateProductDTO;
import guzman.SalesDashboard.dtos.ProductDTO;
import guzman.SalesDashboard.dtos.ProductWithCategoryDTO;
import guzman.SalesDashboard.dtos.CategoryDTO;
import guzman.SalesDashboard.entities.CategoryEntity;
import guzman.SalesDashboard.entities.ProductEntity;
import guzman.SalesDashboard.repositories.CategoryRepository;
import guzman.SalesDashboard.services.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping("/list")
    public ResponseEntity<List<ProductWithCategoryDTO>> getProducts(@RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {

        List<ProductEntity> products = productService.getProductsByFilter(name, categoryId);
        List<ProductWithCategoryDTO> productDTOs = new ArrayList<>();
        
        for (ProductEntity product : products) {
            ProductWithCategoryDTO dto = new ProductWithCategoryDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setStock(product.getStock());
            dto.setImagePath(product.getImagePath());
            
            if (product.getCategory() != null) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                categoryDTO.setDescription(product.getCategory().getDescription());
                dto.setCategory(categoryDTO);
            }
            
            productDTOs.add(dto);
        }
        
        return ResponseEntity.ok(productDTOs);
    }

    @PostMapping("/save")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDTO productDTO) {
        try {
            ProductEntity newProduct = modelMapper.map(productDTO, ProductEntity.class);
            newProduct.setId(null);
            
            CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productDTO.getCategoryId()));
            newProduct.setCategory(category);

            // Si no se proporciona stock, establecer en 0
            if (newProduct.getStock() == null) {
                newProduct.setStock(0);
            }

            // Manejar la imagen - si es una URL de data, guardarla como está
            if (productDTO.getImagePath() != null && !productDTO.getImagePath().trim().isEmpty()) {
                newProduct.setImagePath(productDTO.getImagePath());
            } else {
                // Imagen por defecto
                newProduct.setImagePath("https://via.placeholder.com/300x300?text=No+Image");
            }

            ProductEntity savedProduct = productService.saveProduct(newProduct);
            ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updateProductDTO) {
       
        ProductEntity updatedProduct = modelMapper.map(updateProductDTO, ProductEntity.class);

        CategoryEntity category = categoryRepository.findById(updateProductDTO.getCategoryId())
                .orElseThrow(
                        () -> new RuntimeException("Category not found with ID: " + updateProductDTO.getCategoryId()));
        updatedProduct.setCategory(category);

        ProductEntity result = productService.updateProduct(id, updatedProduct)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        ProductDTO updatedProductDTO = modelMapper.map(result, ProductDTO.class);

        return ResponseEntity.ok(updatedProductDTO);
    }

}
