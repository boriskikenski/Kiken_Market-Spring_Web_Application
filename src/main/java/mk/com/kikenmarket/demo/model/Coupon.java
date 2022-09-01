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

    @ManyToMany
    private List<User> costumer;

    public Coupon(){}
}
