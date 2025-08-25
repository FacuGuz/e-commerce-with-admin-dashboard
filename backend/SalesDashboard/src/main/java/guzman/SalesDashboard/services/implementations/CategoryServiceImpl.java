package guzman.SalesDashboard.services.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import guzman.SalesDashboard.entities.CategoryEntity;
import guzman.SalesDashboard.repositories.CategoryRepository;
import guzman.SalesDashboard.services.CategoryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        if (categoryRepository.count() == 0) {
            throw new RuntimeException("No categories found");
        }
        return categoryRepository.findAll();
    }

    @Override
        public CategoryEntity getCategoryById(Long id) {
            return categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

    @Override
    public CategoryEntity saveCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }
    
    
}
