package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByCart(ShoppingCart shoppingCart);
}
