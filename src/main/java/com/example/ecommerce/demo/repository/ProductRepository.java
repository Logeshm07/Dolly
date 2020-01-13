package com.example.ecommerce.demo.repository;

import com.example.ecommerce.demo.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();
    Product findById(long id);
    List<Product> findAllByBrand(String brand);
    List<Product> findAllByCategory(String category);
    List<Product> findAllByBrandAndCategory(String brand, String category);

    @Query("SELECT DISTINCT category FROM Product")
    List<String> findDistinctCategories();

    @Query("SELECT DISTINCT brand FROM Product")
    List<String> findDistinctBrands();
}