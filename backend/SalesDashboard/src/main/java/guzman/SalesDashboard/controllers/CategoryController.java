package guzman.SalesDashboard.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import guzman.SalesDashboard.dtos.CategoryDTO;
import guzman.SalesDashboard.entities.CategoryEntity;
import guzman.SalesDashboard.services.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(required = false) Long id) {
        try {
            if (id != null) {
                CategoryEntity categoryEntity = categoryService.getCategoryById(id);
                CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
                return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
            }
            // Si no se proporciona ID, devolver todas las categorías
            List<CategoryEntity> categoryEntities = categoryService.getAllCategories();
            List<CategoryDTO> categoryDTOs = modelMapper.map(categoryEntities, new TypeToken<List<CategoryDTO>>() {}.getType());
            return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error in getCategories: " + e.getMessage());
            return new ResponseEntity<>("Error retrieving categories: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            // Log authentication info
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("=== Category Creation Request ===");
            System.out.println("User: " + auth.getName());
            System.out.println("Authorities: " + auth.getAuthorities());
            System.out.println("Category DTO: " + categoryDTO);
            System.out.println("================================");
            
            // Validar que el nombre no esté vacío
            if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
                return new ResponseEntity<>("Category name is required", HttpStatus.BAD_REQUEST);
            }
            
            CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
            CategoryEntity savedCategory = categoryService.saveCategory(categoryEntity);
            CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
            return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error in saveCategory: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error creating category: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        try {
            // Log authentication info
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("=== Category Update Request ===");
            System.out.println("User: " + auth.getName());
            System.out.println("Authorities: " + auth.getAuthorities());
            System.out.println("Category ID: " + id);
            System.out.println("Category DTO: " + categoryDTO);
            System.out.println("================================");
            
            // Validar que el nombre no esté vacío
            if (categoryDTO.getName() == null || categoryDTO.getName().trim().isEmpty()) {
                return new ResponseEntity<>("Category name is required", HttpStatus.BAD_REQUEST);
            }
            
            // Obtener la categoría existente
            CategoryEntity existingCategory = categoryService.getCategoryById(id);
            
            // Actualizar los campos
            existingCategory.setName(categoryDTO.getName());
            existingCategory.setDescription(categoryDTO.getDescription());
            
            // Guardar la categoría actualizada
            CategoryEntity updatedCategory = categoryService.saveCategory(existingCategory);
            CategoryDTO updatedCategoryDTO = modelMapper.map(updatedCategory, CategoryDTO.class);
            
            return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error in updateCategory: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Error updating category: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.err.println("Error in deleteCategory: " + e.getMessage());
            return new ResponseEntity<>("Error deleting category: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test-auth")
    public ResponseEntity<?> testAuth() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return new ResponseEntity<>("Authentication test - User: " + auth.getName() + ", Authorities: " + auth.getAuthorities(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in auth test: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/test-post")
    public ResponseEntity<?> testPost(@RequestBody(required = false) Object body) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("=== Test POST Request ===");
            System.out.println("User: " + auth.getName());
            System.out.println("Authorities: " + auth.getAuthorities());
            System.out.println("Body: " + body);
            System.out.println("========================");
            
            return new ResponseEntity<>("Test POST successful - User: " + auth.getName() + ", Authorities: " + auth.getAuthorities(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error in test POST: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
