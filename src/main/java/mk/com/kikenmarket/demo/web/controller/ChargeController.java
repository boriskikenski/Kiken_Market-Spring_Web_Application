package mk.com.kikenmarket.demo.web.controller;

import com.stripe.exception.StripeException;
import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.ChargeRequest;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.CouponService;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.ProductService;
import mk.com.kikenmarket.demo.service.impl.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChargeController {
    @Autowired
    private StripeService paymentsService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public ChargeController(OrderService orderService, CouponService couponService, ProductService productService,
                            CategoryService categoryService) {
        this.orderService = orderService;
        this.couponService = couponService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest,
                         Model model,
                         @RequestParam Long orderID)
            throws StripeException {
        List<Category> categories = this.categoryService.listAllCategories();
        this.couponService.generateCoupon(orderID);
        String mailMessage = this.orderService.generateMail(orderID);
        this.orderService.sendOrderMail(orderID, mailMessage);

        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);

        this.productService.updateQuantity(orderID);

        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "success-order-page");
        return "template";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("bodyContent", "success-order-page");
        return "template";
    }
}
