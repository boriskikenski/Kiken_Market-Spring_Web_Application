package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.ProductShoppingCart;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JavaMailSender mailSender;

    private final CategoryService categoryService;
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;

    public OrderController(CategoryService categoryService, UserService userService, ShoppingCartService shoppingCartService, OrderService orderService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
    }

    @GetMapping
    private String getOrderPage(Model model, Authentication authentication){
        List<Category> categories = this.categoryService.listAllCategories();
        User costumer = this.userService.getCurrentUser(authentication);

        ShoppingCart shoppingCart =  this.shoppingCartService.findByCustomer(costumer);
        List<ProductShoppingCart> products = this.shoppingCartService
                .findAllByShoppingCart(shoppingCart);

        double total = this.shoppingCartService.getCurrenValue(costumer, products);

        model.addAttribute("total", total);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "order");
        return "template";
    }

    @PostMapping("/confirm")
    private String confirmOrder(Authentication authentication,
                                @RequestParam String email,
                                @RequestParam String street,
                                @RequestParam Long streetNumber,
                                @RequestParam String city,
                                @RequestParam(required = false) Long entryNumber,
                                @RequestParam(required = false) Long apartmentNumber){
        User costumer = this.userService.getCurrentUser(authentication);
        LocalDate today = LocalDate.now();
        String mailMessage = this.orderService.generateMail(costumer);
        this.orderService.createOrder(costumer, today, email, street, streetNumber,
                city, entryNumber, apartmentNumber, mailMessage);
        return "redirect:/successful-order";
    }
}
