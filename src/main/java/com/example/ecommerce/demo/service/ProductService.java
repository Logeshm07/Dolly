package com.example.ecommerce.demo.service;

import com.example.ecommerce.demo.model.Product;
import com.example.ecommerce.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    ProductRepository productRepository;

    public ProductService (ProductRepository productRepository) {this.productRepository = productRepository;}

    public void save(Product product) { productRepository.save(product); }

    public List<String> findDistinctCategories() {
        return productRepository.findDistinctCategories();
    }

    public List<String> findDistinctBrands() {
        return productRepository.findDistinctBrands();
    }

    public List<Product> findByBrandAndOrCategory(String brand, String category) {
        List<Product> productList;
        if(brand == null && category == null) {
            productList = productRepository.findAll();
        }
        else if(brand == null || brand.equals("")) {
            productList = productRepository.findAllByCategory(category);
        }
        else if(category == null || category.equals("")) {
            productList = productRepository.findAllByBrand(brand);
        }
        else {
            productList = productRepository.findAllByBrandAndCategory(brand, category);
        }
        return productList;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(long id) {
        return productRepository.findById(id);
    }
}