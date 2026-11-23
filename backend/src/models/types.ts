/**
 * @author Utkarsh Srivastava
 */
export interface Rating {
  rate: number;
  count: number;
}

export interface Product {
  id: number;
  title: string;
  price: number;
  description: string;
  category: string;
  image: string;
  rating: Rating;
}

export interface CategoryProductsResponse {
  category: string;
  topRated: Product[];
  allProducts: Product[];
}

export interface EnquiryRequest {
  productId: number;
  productTitle: string;
  customerName: string;
  customerEmail: string;
  message: string;
}

export interface EnquiryResponse {
  success: boolean;
  message: string;
  enquiryId: string;
  timestamp: string;
}
