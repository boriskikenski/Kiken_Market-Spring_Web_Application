package mk.com.kikenmarket.demo.model;

import lombok.Data;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;
import mk.com.kikenmarket.demo.model.enumerations.CouponType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponID;

    private double salePercentage;

    private LocalDate expDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @ManyToOne
    private User costumer;

    public Coupon(){}

    public Coupon(User costumer, double salePercentage, CouponStatus status, CouponType couponType) {
        this.costumer = costumer;
        this.salePercentage = salePercentage;
        this.status = status;
        this.couponType = couponType;
    }
}
