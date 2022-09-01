package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findShoppingCartByCostumerAndShoppingCartStatus(User costumer, ShoppingCartStatus shoppingCartStatus);
    List<ShoppingCart> findAllByShoppingCartStatus(ShoppingCartStatus shoppingCartStatus);
}
