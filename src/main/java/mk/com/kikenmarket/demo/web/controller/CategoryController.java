package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    private String getCategoriesPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "categories");
        return "template";

    }

    @GetMapping("/add-category")
    private String getAddCategoryPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "add-category");
        return "template";
    }

    @GetMapping("/edit-page/{id}")
    private String getCategoryEditPage(Model model,
                                       @PathVariable Long id){
        List<Category> categories = this.categoryService.listAllCategories();
        Category category = this.categoryService.findByID(id);

        model.addAttribute("category", category);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "add-category");
        return "template";
    }

    @PostMapping("/add-or-edit-category")
    private String addCategory(@RequestParam String name,
                               @RequestParam String description,
                               @RequestParam String keyWord1,
                               @RequestParam String keyWord2,
                               @RequestParam String keyWord3,
                               @RequestParam(required = false) Long categoryID){
        List<String> keyWords = new ArrayList<>();
        keyWords.add(keyWord1);
        keyWords.add(keyWord2);
        keyWords.add(keyWord3);
        categoryService.saveCategory(name,description,keyWords, categoryID);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    private String deleteCategory(@PathVariable Long id){
        this.categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
