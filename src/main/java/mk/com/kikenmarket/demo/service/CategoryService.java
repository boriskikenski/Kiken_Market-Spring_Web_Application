package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> addCategory(String name, String description, List<String> keyWords);
    List<Category> listAllCategories();
    Category findByID(Long id);
}
