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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import guzman.SalesDashboard.dtos.CategoryDTO;
import guzman.SalesDashboard.entities.CategoryEntity;
import guzman.SalesDashboard.services.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(required = false) Long id) {
        if (id != null) {
            CategoryEntity categoryEntity = categoryService.getCategoryById(id);
            CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        }
        // Si no se proporciona ID, devolver todas las categorías
        List<CategoryEntity> categoryEntities = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = modelMapper.map(categoryEntities, new TypeToken<List<CategoryDTO>>() {}.getType());
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
        CategoryEntity savedCategory = categoryService.saveCategory(categoryEntity);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED); 
    }
}
