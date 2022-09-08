package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Coupon;
import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;
import mk.com.kikenmarket.demo.model.enumerations.CouponType;
import mk.com.kikenmarket.demo.model.exceptions.InvalidCouponException;
import mk.com.kikenmarket.demo.model.exceptions.OrderNotFoundException;
import mk.com.kikenmarket.demo.repository.CouponRepository;
import mk.com.kikenmarket.demo.repository.OrderRepository;
import mk.com.kikenmarket.demo.service.CouponService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.function.Function;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public CouponServiceImpl(CouponRepository couponRepository, OrderRepository orderRepository) {
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void generateCoupon(Long orderID) {
        Order order = this.orderRepository.findByOrderID(orderID)
                .orElseThrow(() -> new OrderNotFoundException(orderID));
        User costumer = order.getCostumer();
        double orderValue = order.getMoneyValue();

        if (orderValue < 1500){
            this.couponRepository.save(new Coupon(costumer, 0, CouponStatus.VALID, CouponType.CUSTOM));
        }else if (orderValue >= 1500 && orderValue < 2500){
            this.couponRepository.save(new Coupon(costumer, 5, CouponStatus.VALID, CouponType.CUSTOM));
        } else if (orderValue >= 2500 && orderValue < 4000) {
            this.couponRepository.save(new Coupon(costumer, 10, CouponStatus.VALID, CouponType.CUSTOM));
        } else if (orderValue >= 4000 && orderValue < 5000) {
            this.couponRepository.save(new Coupon(costumer, 15, CouponStatus.VALID, CouponType.CUSTOM));
        } else if (orderValue >= 5000) {
            this.couponRepository.save(new Coupon(costumer, 20, CouponStatus.VALID, CouponType.CUSTOM));
        }
    }

    @Override
    public Coupon findLastForCostumer(User costumer) {
        Long couponID = this.couponRepository.findAllByCostumer(costumer)
                .stream()
                .map(c->c.getCouponID())
                .max(Comparator.comparing(Function.identity()))
                .get();
        return this.couponRepository.findById(couponID).get();
    }

    @Override
    public Coupon findByIDAndCostumerAndStatus(Long couponID, User costumer, CouponStatus couponStatus) {
        return this.couponRepository.findByCouponIDAndCostumerAndStatus(couponID, costumer, couponStatus)
                .orElseThrow(()->new InvalidCouponException());
    }
}
