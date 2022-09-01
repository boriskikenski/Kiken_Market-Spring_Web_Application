package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryID(Long id);
}
