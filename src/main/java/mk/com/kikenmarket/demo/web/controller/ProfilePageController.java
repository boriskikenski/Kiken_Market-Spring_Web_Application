package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfilePageController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final OrderService orderService;

    public ProfilePageController(CategoryService categoryService, UserService userService, OrderService orderService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping
    private String getProfilePage(Model model,
                                  Authentication authentication) {
        List<Category> categories = this.categoryService.listAllCategories();
        User costumer = this.userService.getCurrentUser(authentication);
        List<Order> orders = this.orderService.listAllOrdersForCostumer(costumer);

        model.addAttribute("categories", categories);
        model.addAttribute("costumer", costumer);
        model.addAttribute("orders", orders);
        model.addAttribute("bodyContent", "profile");
        return "template";
    }
}
