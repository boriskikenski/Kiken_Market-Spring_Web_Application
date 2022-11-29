package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Country;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.CountryService;
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
    private final CountryService countryService;
    public ManufacturerController(ManufacturerService manufacturerService, CategoryService categoryService, CountryService countryService) {
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
        this.countryService = countryService;
    }

    @GetMapping
    private String getManufacturersPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        List<Manufacturer> manufacturers = manufacturerService.listAllManufacturers();

        model.addAttribute("categories", categories);
        model.addAttribute("manufacturers", manufacturers);
        model.addAttribute("bodyContent", "manufacturers");
        return "template";
    }

    @GetMapping("/add-manufacturer")
    private String getAddManufacturerPage(Model model) {
        List<Category> categories = this.categoryService.listAllCategories();
        List<Country> countries = this.countryService.getCountries();

        model.addAttribute("categories", categories);
        model.addAttribute("countries", countries);
        model.addAttribute("bodyContent", "add-manufacturer");
        return "template";
    }

    @GetMapping("/edit-page/{id}")
    private String getManufacturerEditPage(@PathVariable Long id,
                                           Model model){
        List<Country> countries = this.countryService.getCountries();
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
