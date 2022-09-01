package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductID(Long id);
    void deleteProductByProductID(Long id);
}
