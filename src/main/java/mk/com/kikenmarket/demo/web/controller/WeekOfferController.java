package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.model.WeekOffer;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.ProductService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import mk.com.kikenmarket.demo.service.WeekOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/week-offer")
public class WeekOfferController {
    private final WeekOfferService weekOfferService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;

    public WeekOfferController(WeekOfferService weekOfferService,
                               CategoryService categoryService,
                               ProductService productService,
                               ShoppingCartService shoppingCartService) {
        this.weekOfferService = weekOfferService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    private String getWeekOfferPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();

        if (weekOfferService.findByWeekOfferStatus(WeekOfferStatus.ACTIVE) != null) {
            WeekOffer weekOffer = weekOfferService.findByWeekOfferStatus(WeekOfferStatus.ACTIVE);
            List<Product> products = weekOffer.getProductList();
            model.addAttribute("products", products);
        }

        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "week-offer");
        return "template";
    }

    @GetMapping("/selectProducts")
    private String getSelectProductsPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        List<Product> products = this.productService.listAllProducts();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "week-offer-select-products");
        return "template";
    }

    @GetMapping("/selectProducts/set-sale")
    private String getSetSalePage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        List<Product> selectedProducts = this.weekOfferService
                .findByWeekOfferStatus(WeekOfferStatus.CREATING).getProductList();

        model.addAttribute("products", selectedProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "week-offer-set-sale");
        return "template";
    }

    @PostMapping("/selectProducts")
    private String selectProducts(@RequestParam List<Long> selectedProductsIDs){
        List<Product> selectedProducts = this.productService.findAllByID(selectedProductsIDs);
        this.weekOfferService.save(selectedProducts);
        return "redirect:/week-offer/selectProducts/set-sale";
    }

    @PostMapping("/selectProducts/set-sale")
    private String setSale(@RequestParam List<Double> sale){
        this.weekOfferService.setProductSale(sale);
        this.shoppingCartService.updateProductShoppingCart();
        return "redirect:/week-offer";
    }
}
