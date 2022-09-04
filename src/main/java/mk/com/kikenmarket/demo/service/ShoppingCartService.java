package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.ProductShoppingCart;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {
    boolean addProductToShoppingCart(User costumer, Long productID, double quantity);
    List<ProductShoppingCart> findAllByShoppingCart(ShoppingCart shoppingCart);
    ShoppingCart findByCustomer(User costumer);
    double getCurrenValue(User costumer, List<ProductShoppingCart> productShoppingCart);
    void deleteFromShoppingCart(Long productID, User costumer);
    void updateProductShoppingCart();
    void deleteAllProductsFromCart(User costumer);
}
