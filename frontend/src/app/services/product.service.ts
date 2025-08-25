import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product, Category, ProductCreateRequest, ProductUpdateRequest, CategoryCreateRequest, CategoryUpdateRequest } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private readonly API_URL = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  // Product methods
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/products`);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.API_URL}/products/${id}`);
  }

  createProduct(product: ProductCreateRequest): Observable<Product> {
    return this.http.post<Product>(`${this.API_URL}/products`, product);
  }

  updateProduct(product: ProductUpdateRequest): Observable<Product> {
    return this.http.put<Product>(`${this.API_URL}/products/${product.id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/products/${id}`);
  }

  // Category methods
  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.API_URL}/categories`);
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.API_URL}/categories/${id}`);
  }

  createCategory(category: CategoryCreateRequest): Observable<Category> {
    return this.http.post<Category>(`${this.API_URL}/categories`, category);
  }

  updateCategory(category: CategoryUpdateRequest): Observable<Category> {
    return this.http.put<Category>(`${this.API_URL}/categories/${category.id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/categories/${id}`);
  }

  // Search and filter methods
  searchProducts(query: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/products/search?q=${query}`);
  }

  getProductsByCategory(categoryId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.API_URL}/categories/${categoryId}/products`);
  }
}
