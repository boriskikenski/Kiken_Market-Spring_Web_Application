package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.ChargeRequest;
import mk.com.kikenmarket.demo.model.Coupon;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;
import mk.com.kikenmarket.demo.service.CouponService;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final UserService userService;
    private final OrderService orderService;
    private final CouponService couponService;

    public CheckoutController(UserService userService, OrderService orderService, CouponService couponService) {
        this.userService = userService;
        this.orderService = orderService;
        this.couponService = couponService;
    }

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @PostMapping
    private String checkout(Model model,
                            Authentication authentication,
                            @RequestParam double totalToPay,
                            @RequestParam String email,
                            @RequestParam String street,
                            @RequestParam Long streetNumber,
                            @RequestParam String city,
                            @RequestParam(required = false) Long entryNumber,
                            @RequestParam(required = false) Long apartmentNumber,
                            @RequestParam(required = false) Long couponID){
        User costumer = this.userService.getCurrentUser(authentication);
        LocalDate today = LocalDate.now();
        Coupon coupon = this.couponService.findByIDAndCostumerAndStatus(couponID, costumer, CouponStatus.VALID);
        Long orderID = this.orderService.createOrder(costumer, today, email, street, streetNumber,
                city, entryNumber, apartmentNumber, coupon);

        model.addAttribute("orderID", orderID);
        model.addAttribute("amount", (int)(totalToPay - (totalToPay * coupon.getSalePercentage()/100)));
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);
        model.addAttribute("bodyContent", "checkout");
        return "template";
    }
}
