package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.ChargeRequest;
import mk.com.kikenmarket.demo.model.Coupon;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;
import mk.com.kikenmarket.demo.service.CategoryService;
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
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final UserService userService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final CategoryService categoryService;

    public CheckoutController(UserService userService, OrderService orderService, CouponService couponService,
                              CategoryService categoryService) {
        this.userService = userService;
        this.orderService = orderService;
        this.couponService = couponService;
        this.categoryService = categoryService;
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
        List<Category> categories = this.categoryService.listAllCategories();
        User costumer = this.userService.getCurrentUser(authentication);
        LocalDate today = LocalDate.now();

        Long orderID;
        if(couponID!=null){
            Coupon coupon = this.couponService.findByIDAndCostumerAndStatus(couponID, costumer, CouponStatus.VALID);
            orderID = this.orderService.createOrder(costumer, today, email, street, streetNumber,
                    city, entryNumber, apartmentNumber, coupon);
            model.addAttribute("amount", (int)(totalToPay - (totalToPay * coupon.getSalePercentage()/100)));
        } else {
            orderID = this.orderService.createOrder(costumer, today, email, street, streetNumber,
                    city, entryNumber, apartmentNumber, null);
            model.addAttribute("amount", (int)totalToPay);
        }

        model.addAttribute("categories", categories);
        model.addAttribute("orderID", orderID);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);
        model.addAttribute("bodyContent", "checkout");
        return "template";
    }
}
