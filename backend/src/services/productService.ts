/**
 * @author Utkarsh Srivastava
 */
import axios from 'axios';
import { Product, CategoryProductsResponse } from '../models/types';

const FAKE_STORE_API = process.env.FAKE_STORE_API_URL || 'https://fakestoreapi.com';

export class ProductService {
  /**
   * Fetch all products from Fake Store API
   */
  async getAllProducts(): Promise<Product[]> {
    const response = await axios.get<Product[]>(`${FAKE_STORE_API}/products`);
    return response.data;
  }

  /**
   * Fetch 5 top-rated products overall
   */
  async getTopRatedProducts(limit: number = 5): Promise<Product[]> {
    const products = await this.getAllProducts();
    return [...products]
      .sort((a, b) => b.rating.rate - a.rating.rate)
      .slice(0, limit);
  }

  /**
   * Fetch all categories
   */
  async getCategories(): Promise<string[]> {
    const response = await axios.get<string[]>(`${FAKE_STORE_API}/products/categories`);
    return response.data;
  }

  /**
   * Fetch products for a category, separating top-rated from rest
   */
  async getProductsByCategory(category: string): Promise<CategoryProductsResponse> {
    const response = await axios.get<Product[]>(
      `${FAKE_STORE_API}/products/category/${encodeURIComponent(category)}`
    );
    const products = response.data;
    const sorted = [...products].sort((a, b) => b.rating.rate - a.rating.rate);
    const topRated = sorted.slice(0, 3);

    return {
      category,
      topRated,
      allProducts: products,
    };
  }

  /**
   * Search products by query string matching title or description
   */
  async searchProducts(query: string): Promise<Product[]> {
    if (!query || query.trim() === '') {
      return [];
    }
    const products = await this.getAllProducts();
    const q = query.toLowerCase().trim();
    return products.filter(
      (p) =>
        p.title.toLowerCase().includes(q) ||
        p.description.toLowerCase().includes(q) ||
        p.category.toLowerCase().includes(q)
    );
  }

  /**
   * Fetch single product details
   */
  async getProductById(id: number): Promise<Product | null> {
    try {
      const response = await axios.get<Product>(`${FAKE_STORE_API}/products/${id}`);
      return response.data;
    } catch (error) {
      return null;
    }
  }
}
