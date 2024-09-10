package com.example.project_3.service;

import com.example.project_3.entity.Product;
import com.example.project_3.entity.Shop;
import com.example.project_3.repo.ProductRepository;
import com.example.project_3.repo.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    public ProductService(ProductRepository productRepository, ShopRepository shopRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }
    public Product addProduct(Long shopId, Product product) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (!shopOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
        product.setShop(shopOptional.get());
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Long shopId, Product updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Check if the product belongs to the given shop
            if (!product.getShop().getId().equals(shopId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product does not belong to the specified shop");
            }

            // Update product details
            product.setName(updatedProduct.getName());
            product.setImage(updatedProduct.getImage());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());

            return productRepository.save(product);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }


    public void deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    public List<Product> getProductsByShop(Long shopId) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            return productRepository.findByShop(shopOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found");
        }
    }


}
