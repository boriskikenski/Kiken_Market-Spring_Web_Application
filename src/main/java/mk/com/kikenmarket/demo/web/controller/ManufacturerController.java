package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Country;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.ManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;

    public ManufacturerController(ManufacturerService manufacturerService, CategoryService categoryService) {
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
    }

    @GetMapping
    private String getManufacturersPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        List<Manufacturer> manufacturers = manufacturerService.listAllManufacturers();
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("bodyContent", "manufacturers");
        return "template";
    }

    @GetMapping("/add-manufacturer")
    private String getAddManufacturerPage(Model model) {
        String url = "https://restcountries.com/v3.1/all";
        RestTemplate restTemplate = new RestTemplate();

        Country[] countries = restTemplate.getForObject(url, Country[].class);
        List<Category> categories = this.categoryService.listAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("countries", countries);
        model.addAttribute("bodyContent", "add-manufacturer");
        return "template";
    }

    @GetMapping("/edit-page/{id}")
    private String getManufacturerEditPage(@PathVariable Long id,
                                           Model model){
        String url = "https://restcountries.com/v3.1/all";
        RestTemplate restTemplate = new RestTemplate();

        Country[] countries = restTemplate.getForObject(url, Country[].class);
        List<Category> categories = this.categoryService.listAllCategories();

        Manufacturer manufacturer = this.manufacturerService.findByID(id);

        model.addAttribute("manufacturer", manufacturer);
        model.addAttribute("categories", categories);
        model.addAttribute("countries", countries);
        model.addAttribute("bodyContent", "add-manufacturer");
        return "template";
    }

    @PostMapping("/add-or-edit-manufacturer")
    private String addManufacturer(@RequestParam String name,
                                   @RequestParam String country,
                                   @RequestParam(required = false) Long manufacturerID){
        manufacturerService.saveManufacturer(name,country, manufacturerID);
        return "redirect:/manufacturers";
    }

    @PostMapping("/delete/{id}")
    private String deleteManufacturer(@PathVariable Long id){
        this.manufacturerService.deleteManufacturer(id);
        return "redirect:/manufacturers";
    }
}
