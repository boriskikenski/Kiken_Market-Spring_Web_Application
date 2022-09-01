package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.WeekOffer;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekOfferRepository extends JpaRepository<WeekOffer, Long> {
    WeekOffer findByOfferStatus(WeekOfferStatus status);
    List<WeekOffer> findAll();
}
