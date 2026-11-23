/**
 * @author Utkarsh Srivastava
 */
import express, { Request, Response, NextFunction } from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import { ProductService } from './services/productService';
import { MockEmailService } from './services/emailService';
import { EnquiryRequest } from './models/types';

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

const productService = new ProductService();
const emailService = new MockEmailService();

// Health Check
app.get('/health', (req: Request, res: Response) => {
  res.json({ status: 'ok', service: 'Shopora Backend Gateway' });
});

// GET /api/products - Get all products
app.get('/api/products', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const products = await productService.getAllProducts();
    res.json(products);
  } catch (error) {
    next(error);
  }
});

// GET /api/products/featured - Get 5 top-rated products
app.get('/api/products/featured', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const topRated = await productService.getTopRatedProducts(5);
    res.json(topRated);
  } catch (error) {
    next(error);
  }
});

// GET /api/products/categories - Get product categories
app.get('/api/products/categories', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const categories = await productService.getCategories();
    res.json(categories);
  } catch (error) {
    next(error);
  }
});

// GET /api/products/category/:category - Get products by category
app.get('/api/products/category/:category', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const category = req.params.category;
    const result = await productService.getProductsByCategory(category);
    res.json(result);
  } catch (error) {
    next(error);
  }
});

// GET /api/products/search - Search products by query term ?q=...
app.get('/api/products/search', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const query = (req.query.q as string) || '';
    const results = await productService.searchProducts(query);
    res.json(results);
  } catch (error) {
    next(error);
  }
});

// GET /api/products/:id - Single product details
app.get('/api/products/:id', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (isNaN(id)) {
      res.status(400).json({ error: 'Invalid product ID' });
      return;
    }
    const product = await productService.getProductById(id);
    if (!product) {
      res.status(404).json({ error: 'Product not found' });
      return;
    }
    res.json(product);
  } catch (error) {
    next(error);
  }
});

// POST /api/enquiries - Submit product enquiry
app.post('/api/enquiries', async (req: Request, res: Response, next: NextFunction) => {
  try {
    const { productId, productTitle, customerName, customerEmail, message }: EnquiryRequest = req.body;

    if (!productId || !customerName || !customerEmail || !message) {
      res.status(400).json({
        error: 'Missing required fields: productId, customerName, customerEmail, and message are required.',
      });
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(customerEmail)) {
      res.status(400).json({ error: 'Invalid email address format' });
      return;
    }

    const response = await emailService.sendEnquiryEmail({
      productId,
      productTitle: productTitle || 'Product',
      customerName,
      customerEmail,
      message,
    });

    res.status(201).json(response);
  } catch (error) {
    next(error);
  }
});

// Centralized error handler
app.use((err: Error, req: Request, res: Response, next: NextFunction) => {
  console.error('[SERVER ERROR]:', err);
  res.status(500).json({
    error: 'Internal Server Error',
    message: err.message,
  });
});

app.listen(Number(PORT), '0.0.0.0', () => {
  console.log(`🚀 Shopora Backend Gateway running on http://0.0.0.0:${PORT}`);
  console.log(`👉 Health check: http://localhost:${PORT}/health`);
});
