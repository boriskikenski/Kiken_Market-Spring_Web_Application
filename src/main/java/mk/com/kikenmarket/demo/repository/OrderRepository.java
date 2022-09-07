package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByCart(ShoppingCart shoppingCart);
    List<Order> findAllByCostumer(User costumer);
    Optional<Order> findByOrderID(Long id);
}
