package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.ProductShoppingCart;
import mk.com.kikenmarket.demo.model.ProductShoppingCartID;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductShoppingCartRepository extends JpaRepository<ProductShoppingCart, ProductShoppingCartID> {
    ProductShoppingCart findProductShoppingCartById(ProductShoppingCartID id);
    List<ProductShoppingCart> findAllByShoppingCart(ShoppingCart shoppingCart);
    void deleteById(ProductShoppingCartID productShoppingCartID);
}
