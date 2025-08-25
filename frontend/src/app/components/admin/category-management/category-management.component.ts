import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { Category, CategoryCreateRequest, CategoryUpdateRequest } from '../../../interfaces';

@Component({
  selector: 'app-category-management',
  templateUrl: './category-management.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class CategoryManagementComponent implements OnInit {
  categories: Category[] = [];
  isLoading = false;
  showForm = false;
  isEditing = false;
  selectedCategory: Category | null = null;

  categoryForm: FormGroup;

  constructor(
    private productService: ProductService,
    private fb: FormBuilder
  ) {
    this.categoryForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: ['', [Validators.required, Validators.minLength(5)]]
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.isLoading = true;
    this.productService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        this.isLoading = false;
      }
    });
  }

  onCreate(): void {
    this.isEditing = false;
    this.selectedCategory = null;
    this.categoryForm.reset();
    this.showForm = true;
  }

  onEdit(category: Category): void {
    this.isEditing = true;
    this.selectedCategory = category;
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description
    });
    this.showForm = true;
  }

  onDelete(category: Category): void {
    if (confirm(`¿Estás seguro de que quieres eliminar la categoría "${category.name}"?`)) {
      this.productService.deleteCategory(category.id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (error) => {
          console.error('Error deleting category:', error);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      const formData = this.categoryForm.value;
      
      if (this.isEditing && this.selectedCategory) {
        const updateRequest: CategoryUpdateRequest = {
          id: this.selectedCategory.id,
          name: formData.name,
          description: formData.description
        };
        
        this.productService.updateCategory(updateRequest).subscribe({
          next: () => {
            this.loadCategories();
            this.showForm = false;
          },
          error: (error) => {
            console.error('Error updating category:', error);
          }
        });
      } else {
        const createRequest: CategoryCreateRequest = {
          name: formData.name,
          description: formData.description
        };
        
        this.productService.createCategory(createRequest).subscribe({
          next: () => {
            this.loadCategories();
            this.showForm = false;
          },
          error: (error) => {
            console.error('Error creating category:', error);
          }
        });
      }
    }
  }

  onCancel(): void {
    this.showForm = false;
    this.categoryForm.reset();
  }
}
