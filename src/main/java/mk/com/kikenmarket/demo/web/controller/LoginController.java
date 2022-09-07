package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final CategoryService categoryService;
    private final UserService userService;

    public LoginController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String getLoginPage(Model model, Authentication authentication){
        if (authentication != null) {
            return "redirect:/home";
        }
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "login");
        return "template";
    }
}
