package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Coupon;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findAllByCostumer(User costumer);
    Optional<Coupon> findByCouponIDAndCostumerAndStatus(Long id, User costumer, CouponStatus status);
}
