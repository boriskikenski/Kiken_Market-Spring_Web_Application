package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Role;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;
    private final CategoryService categoryService;

    public RegisterController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getRegisterPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "register");
        return "template";

    }

    @PostMapping
    public String register(@RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String street,
                           @RequestParam String city,
                           @RequestParam int streetNumber,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatPassword,
                           @RequestParam String role) {

        userService.register(username, password, repeatPassword, name, surname, street, streetNumber, city, role);
        return "redirect:/login";
    }
}
