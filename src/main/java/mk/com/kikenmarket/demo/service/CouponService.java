package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Coupon;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;

public interface CouponService {
    void generateCoupon(Long orderID);
    Coupon findLastForCostumer(User costumer);
    Coupon findByIDAndCostumerAndStatus(Long couponID, User costumer, CouponStatus couponStatus);
}
