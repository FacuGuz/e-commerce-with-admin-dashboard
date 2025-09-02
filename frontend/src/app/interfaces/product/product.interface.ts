export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  imagePath: string;
  category: Category;
  isActive: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export interface Category {
  id: number;
  name: string;
  description: string;
  isActive: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export interface ProductFilter {
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  searchTerm?: string;
  sortBy?: 'name' | 'price' | 'createdAt';
  sortOrder?: 'asc' | 'desc';
}

export interface ProductCreateRequest {
  name: string;
  description: string;
  price: number;
  stock: number;
  categoryId: number;
  imagePath: string;
}

export interface ProductUpdateRequest {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  categoryId: number;
}

export interface CategoryCreateRequest {
  name: string;
  description: string;
}

export interface CategoryUpdateRequest {
  id: number;
  name: string;
  description: string;
}
