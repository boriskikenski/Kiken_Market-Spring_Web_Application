package mk.com.kikenmarket.demo.web.controller;

import com.stripe.exception.StripeException;
import mk.com.kikenmarket.demo.model.ChargeRequest;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.impl.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChargeController {
    @Autowired
    private StripeService paymentsService;
    private final OrderService orderService;

    public ChargeController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/charge")
    public String charge(ChargeRequest chargeRequest,
                         Model model,
                         @RequestParam Long orderID)
            throws StripeException {
        String mailMessage = this.orderService.generateMail(orderID);
        this.orderService.sendOrderMail(orderID, mailMessage);

        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);

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
