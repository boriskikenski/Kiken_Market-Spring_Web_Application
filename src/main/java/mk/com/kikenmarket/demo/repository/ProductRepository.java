package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductID(Long id);
    void deleteProductByProductID(Long id);
    List<Product> findAllByManufacturer(Manufacturer manufacturer);
    List<Product> findAllByCategories(Category category);
    List<Product> findAllByManufacturerAndCategories(Manufacturer manufacturer, Category category);
}
