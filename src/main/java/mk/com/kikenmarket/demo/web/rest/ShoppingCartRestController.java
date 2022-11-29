package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.ProductShoppingCart;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/shopping-cart")
public class ShoppingCartRestController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public ShoppingCartRestController(ShoppingCartService shoppingCartService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @GetMapping
    private ResponseEntity<ShoppingCart> findShoppingCart(Authentication authentication){
        ShoppingCart shoppingCart = this.shoppingCartService
                .findByCustomer(this.userService.getCurrentUser(authentication));
        if (shoppingCart != null) {
            return ResponseEntity.ok().body(shoppingCart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/value")
    private double getShoppingCartValue(Authentication authentication){
        User costumer = this.userService.getCurrentUser(authentication);
        ShoppingCart shoppingCart =  this.shoppingCartService.findByCustomer(costumer);
        List<ProductShoppingCart> products = this.shoppingCartService
                .findAllByShoppingCart(shoppingCart);

        return this.shoppingCartService.getCurrenValue(costumer, products);
    }

    @PostMapping("/added/{id}")
    private boolean addProductToShoppingCart(Authentication authentication,
                                            @PathVariable Long id,
                                            @RequestParam double quantity){
        User costumer = this.userService.getCurrentUser(authentication);
        return this.shoppingCartService.addProductToShoppingCart(costumer, id, quantity);
    }

    @PostMapping ("/delete/{id}")
    private ResponseEntity<ShoppingCart> deleteFromShoppingCart(Authentication authentication,
                                          @PathVariable Long id){
        User costumer = this.userService.getCurrentUser(authentication);
        this.shoppingCartService.deleteFromShoppingCart(id, costumer);
        return ResponseEntity.ok().build();
    }
 }
