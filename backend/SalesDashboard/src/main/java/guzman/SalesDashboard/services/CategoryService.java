package guzman.SalesDashboard.services;

import java.util.List;

import guzman.SalesDashboard.entities.CategoryEntity;

public interface CategoryService {
    public CategoryEntity saveCategory(CategoryEntity category);
    public List<CategoryEntity> getAllCategories();
    public CategoryEntity getCategoryById(Long id);
    public void deleteCategory(Long id);
}