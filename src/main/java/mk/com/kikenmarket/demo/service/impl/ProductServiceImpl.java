package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.repository.ProductRepository;
import mk.com.kikenmarket.demo.service.ProductService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ShoppingCartService shoppingCartService;

    public ProductServiceImpl(ProductRepository productRepository, ShoppingCartService shoppingCartService) {
        this.productRepository = productRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public void saveProduct(Long productID, String name, int quantity, double price,
                            Manufacturer manufacturer, Category categories,
                            Date expirationDate, String productImageURL) {
        if (productID == null){
            this.productRepository.save(new Product(name, quantity, price, manufacturer, categories,
                    expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    productImageURL));
        } else  {
            Product product = this.productRepository.findByProductID(productID);
            product.setName(name);
            product.setQuantity(quantity);
            product.setPrice(price);
            product.setManufacturer(manufacturer);
            product.setCategories(categories);
            product.setExpirationDate(expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            product.setProductImageURL(productImageURL);
            this.productRepository.save(product);
            this.shoppingCartService.updateProductShoppingCart();
        }
    }

    @Override
    public List<Product> listAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> findAllByID(List<Long> listOfProductIDs) {
        List<Product> selectedProducts = listOfProductIDs.stream()
                .map(productID -> this.productRepository.findByProductID(productID))
                .collect(Collectors.toList());
        return selectedProducts;
    }

    @Override
    public Product findByID(Long id) {
        return this.productRepository.findByProductID(id);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        this.productRepository.deleteProductByProductID(id);
    }
}
