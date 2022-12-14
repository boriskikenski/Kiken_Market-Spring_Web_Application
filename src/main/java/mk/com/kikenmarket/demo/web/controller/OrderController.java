package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.*;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/reorder/{id}")
    private String reorder(@PathVariable Long id,
                           Authentication authentication){
        User costumer = this.userService.getCurrentUser(authentication);
        this.shoppingCartService.deleteAllProductsFromCart(costumer);
        return "redirect:/order/reorderAll?orderID="+id;
    }

    @GetMapping ("/reorderAll")
    private String reorderAddProducts(Authentication authentication,
                                      Long orderID){
        User costumer = this.userService.getCurrentUser(authentication);
        this.orderService.reorder(orderID, costumer);
        return "redirect:/shopping-cart";
    }
}
