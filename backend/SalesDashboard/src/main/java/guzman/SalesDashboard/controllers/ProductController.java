package guzman.SalesDashboard.controllers;

import java.util.List;

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

import guzman.SalesDashboard.dtos.CreateProductDTO;
import guzman.SalesDashboard.dtos.ProductDTO;
import guzman.SalesDashboard.entities.CategoryEntity;
import guzman.SalesDashboard.entities.ProductEntity;
import guzman.SalesDashboard.repositories.CategoryRepository;
import guzman.SalesDashboard.services.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId) {

        List<ProductEntity> products = productService.getProductsByFilter(name, categoryId);
        List<ProductDTO> productDTOs = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {
        }.getType());
        return ResponseEntity.ok(productDTOs);
    }

    @PostMapping("/save")
    public ResponseEntity<ProductDTO> createProduct( @RequestBody CreateProductDTO productDTO) {
        ProductEntity newProduct = modelMapper.map(productDTO, ProductEntity.class);
        newProduct.setId(null);
        CategoryEntity category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + productDTO.getCategoryId()));
        newProduct.setCategory(category);

        newProduct.setStock(0);

        ProductEntity savedProduct = productService.saveProduct(newProduct);

        ProductDTO savedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
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
