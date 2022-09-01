package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final CategoryService categoryService;

    public LoginController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getLoginPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "login");
        return "template";

    }
}
