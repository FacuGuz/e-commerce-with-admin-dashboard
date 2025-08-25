import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../services/product.service';
import { Product, Category, ProductCreateRequest, ProductUpdateRequest } from '../../../interfaces';

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule]
})
export class ProductManagementComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  isLoading = false;
  showForm = false;
  isEditing = false;
  selectedProduct: Product | null = null;
  selectedImage: File | null = null;
  imagePreview: string | null = null;

  productForm: FormGroup;

  constructor(
    private productService: ProductService,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      price: ['', [Validators.required, Validators.min(0)]],
      stock: ['', [Validators.required, Validators.min(0)]],
      categoryId: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(): void {
    this.isLoading = true;
    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products = products;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading products:', error);
        this.isLoading = false;
      }
    });
  }

  loadCategories(): void {
    this.productService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
      }
    });
  }

  onCreate(): void {
    this.isEditing = false;
    this.selectedProduct = null;
    this.productForm.reset();
    this.selectedImage = null;
    this.imagePreview = null;
    this.showForm = true;
  }

  onEdit(product: Product): void {
    this.isEditing = true;
    this.selectedProduct = product;
    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      stock: product.stock,
      categoryId: product.category.id
    });
    this.imagePreview = product.imageUrl;
    this.showForm = true;
  }

  onDelete(product: Product): void {
    if (confirm(`¿Estás seguro de que quieres eliminar el producto "${product.name}"?`)) {
      this.productService.deleteProduct(product.id).subscribe({
        next: () => {
          this.loadProducts();
        },
        error: (error) => {
          console.error('Error deleting product:', error);
        }
      });
    }
  }

  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const formData = this.productForm.value;
      
      if (this.isEditing && this.selectedProduct) {
        const updateRequest: ProductUpdateRequest = {
          id: this.selectedProduct.id,
          name: formData.name,
          description: formData.description,
          price: formData.price,
          stock: formData.stock,
          categoryId: formData.categoryId
        };
        
        this.productService.updateProduct(updateRequest).subscribe({
          next: () => {
            this.loadProducts();
            this.showForm = false;
          },
          error: (error) => {
            console.error('Error updating product:', error);
          }
        });
      } else {
        const createRequest: ProductCreateRequest = {
          name: formData.name,
          description: formData.description,
          price: formData.price,
          stock: formData.stock,
          categoryId: formData.categoryId,
          imageUrl: this.imagePreview || ''
        };
        
        this.productService.createProduct(createRequest).subscribe({
          next: () => {
            this.loadProducts();
            this.showForm = false;
          },
          error: (error) => {
            console.error('Error creating product:', error);
          }
        });
      }
    }
  }

  onCancel(): void {
    this.showForm = false;
    this.productForm.reset();
    this.selectedImage = null;
    this.imagePreview = null;
  }
}
