package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.ManufacturerService;
import mk.com.kikenmarket.demo.service.ProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;

    public ProductRestController(ProductService productService, ManufacturerService manufacturerService, CategoryService categoryService) {
        this.productService = productService;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
    }

    @GetMapping
    private List<Product> listAllProducts(){
        return this.productService.listAllProducts();
    }

    @GetMapping("/filter")
    private List<Product> listAllProductsFiltered(@RequestParam(required = false) Long manufacturerID,
                                                  @RequestParam(required = false) Long categoryID){
        return this.productService.listAllProductsFiltered(manufacturerID, categoryID);
    }

    @GetMapping("/search")
    private List<Product> searchProduct(@RequestParam String search){
        return this.productService.search(search);
    }

    @GetMapping("/find/{id}")
    private ResponseEntity<Product> findProductByID(@PathVariable Long id){
        Product product = this.productService.findByID(id);
        if (product != null){
            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    private ResponseEntity<Product> addProduct(@RequestParam String name,
                                               @RequestParam int quantity,
                                               @RequestParam double price,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expDate,
                                               @RequestParam Long manufacturer,
                                               @RequestParam Long category,
                                               @RequestParam String productImageURL){
        Manufacturer man = manufacturerService.findByID(manufacturer);
        Category cat = categoryService.findByID(category);
        productService.saveProduct(null, name, quantity, price, man, cat, expDate, productImageURL);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/{id}")
    private ResponseEntity<Product> editProduct(@RequestParam String name,
                                                @RequestParam int quantity,
                                                @RequestParam double price,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date expDate,
                                                @RequestParam Long manufacturer,
                                                @RequestParam Long category,
                                                @RequestParam String productImageURL,
                                                @PathVariable Long id){
        if (this.productService.findByID(id) != null){
            Manufacturer man = manufacturerService.findByID(manufacturer);
            Category cat = categoryService.findByID(category);
            productService.saveProduct(id, name, quantity, price, man, cat, expDate, productImageURL);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/delete/{id}")
    private ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        if (this.productService.findByID(id) != null){
            this.productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}