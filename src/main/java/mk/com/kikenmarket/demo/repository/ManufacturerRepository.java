package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByManufacturerID(Long id);
}
