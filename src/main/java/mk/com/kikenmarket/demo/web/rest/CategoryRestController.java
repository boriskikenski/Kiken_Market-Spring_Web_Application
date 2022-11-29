package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    private List<Category> listAllCategories(){
        return this.categoryService.listAllCategories();
    }

    @GetMapping("/find/{id}")
    private ResponseEntity<Category> findByID(@PathVariable Long id){
        Category category = this.categoryService.findByID(id);
        if (category != null) {
            return ResponseEntity.ok().body(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    private ResponseEntity<Category> addCategory(@RequestParam String name,
                                                 @RequestParam String description,
                                                 @RequestParam String keyWord1,
                                                 @RequestParam String keyWord2,
                                                 @RequestParam String keyWord3){
        List<String> keyWords = new ArrayList<>();
        keyWords.add(keyWord1);
        keyWords.add(keyWord2);
        keyWords.add(keyWord3);
        categoryService.saveCategory(name,description,keyWords, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/{id}")
    private ResponseEntity<Category> editCategory(@PathVariable Long id,
                                                  @RequestParam String name,
                                                  @RequestParam String description,
                                                  @RequestParam String keyWord1,
                                                  @RequestParam String keyWord2,
                                                  @RequestParam String keyWord3){
        List<String> keyWords = new ArrayList<>();
        keyWords.add(keyWord1);
        keyWords.add(keyWord2);
        keyWords.add(keyWord3);
        categoryService.saveCategory(name,description,keyWords, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{id}")
    private ResponseEntity<Category> deleteCategory(@PathVariable Long id){
        if (this.categoryService.findByID(id) != null){
            this.categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
