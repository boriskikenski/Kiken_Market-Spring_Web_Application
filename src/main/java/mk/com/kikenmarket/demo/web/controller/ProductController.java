package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.ManufacturerService;
import mk.com.kikenmarket.demo.service.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;
    private final ProductService productService;

    public ProductController(CategoryService categoryService, ManufacturerService manufacturerService,
                             ProductService productService) {
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
        this.productService = productService;
    }

    @GetMapping("/all-products")
    private String getProductsPage(Model model){
        List<Product> products = this.productService.listAllProducts();
        List<Category> categories = this.categoryService.listAllCategories();
        List<Manufacturer> manufacturers = this.manufacturerService.listAllManufacturers();

        model.addAttribute("products", products);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "products");
        return "template";
    }

    @GetMapping("/add-product")
    private String getAddProductsPage(Model model){
        List<Manufacturer> manufacturers = this.manufacturerService.listAllManufacturers();
        List<Category> categories = this.categoryService.listAllCategories();

        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "add-product");
        return "template";
    }

    @GetMapping("/edit-page/{id}")
    private String getProductEditPage(Model model,
                                      @PathVariable Long id){
        List<Category> categories = this.categoryService.listAllCategories();
        List<Manufacturer> manufacturers = this.manufacturerService.listAllManufacturers();
        Product product = this.productService.findByID(id);

        model.addAttribute("product", product);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "add-product");
        return "template";
    }

    @PostMapping("add-or-edit-product")
    private String addProduct(@RequestParam(required = false) Long productID,
                              @RequestParam String name,
                              @RequestParam int quantity,
                              @RequestParam double price,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expDate,
                              @RequestParam Long manufacturer,
                              @RequestParam Long category,
                              @RequestParam String productImageURL){
        Manufacturer man = manufacturerService.findByID(manufacturer);
        Category cat = categoryService.findByID(category);
        productService.saveProduct(productID, name, quantity, price, man, cat, expDate, productImageURL);
        return "redirect:/products/all-products";
    }

    @PostMapping("/delete/{id}")

    private String deleteProduct(@PathVariable Long id){
        this.productService.deleteProduct(id);
        return "redirect:/products/all-products";
    }
}
