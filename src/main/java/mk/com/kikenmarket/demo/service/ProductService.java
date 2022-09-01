package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.Product;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    void saveProduct(Long productID, String name, int quantity, double price,
                     Manufacturer manufacturer, Category categories,
                     Date expirationDate, String productImageURL);

    List<Product> listAllProducts();

    List<Product> findAllByID(List<Long> listOfProductIDs);

    Product findByID(Long id);

    void deleteProduct(Long id);
}
