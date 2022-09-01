package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.*;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CategoryService categoryService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, CategoryService categoryService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    private String getShoppingCartPage(Model model, Authentication authentication){
        List<Category> categories = this.categoryService.listAllCategories();
        User costumer = this.userService.getCurrentUser(authentication);

        ShoppingCart shoppingCart =  this.shoppingCartService.findByCustomer(costumer);
        List<ProductShoppingCart> products = this.shoppingCartService
                .findAllByShoppingCart(shoppingCart);

        double total = this.shoppingCartService.getCurrenValue(costumer, products);

        model.addAttribute("total", total);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "shopping-cart");
        return "template";
    }

    @PostMapping("/added/{id}")
    private String addProductToShoppingCart(Authentication authentication,
                                            @PathVariable Long id,
                                            @RequestParam double quantity){
        User costumer = this.userService.getCurrentUser(authentication);

        boolean alreadyInShoppingCart = this.shoppingCartService.addProductToShoppingCart(costumer, id, quantity);
        if (alreadyInShoppingCart){
            return "redirect:/shopping-cart";
        }else {
            return "redirect:/products/all-products";
        }
    }

    @PostMapping ("/delete/{id}")
    private String deleteFromShoppingCart(Authentication authentication,
                                          @PathVariable Long id){
        User costumer = this.userService.getCurrentUser(authentication);
        this.shoppingCartService.deleteFromShoppingCart(id, costumer);

        return "redirect:/shopping-cart";
    }
}
