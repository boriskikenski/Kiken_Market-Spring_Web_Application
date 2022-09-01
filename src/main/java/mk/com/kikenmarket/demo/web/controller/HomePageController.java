package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Announcement;
import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.model.WeekOffer;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;
import mk.com.kikenmarket.demo.service.AnnouncementService;
import mk.com.kikenmarket.demo.service.CategoryService;
import mk.com.kikenmarket.demo.service.WeekOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = {"/", "/home", ""})
public class HomePageController {
    private final CategoryService categoryService;
    private final WeekOfferService weekOfferService;
    private final AnnouncementService announcementService;

    public HomePageController(CategoryService categoryService, WeekOfferService weekOfferService, AnnouncementService announcementService) {
        this.categoryService = categoryService;
        this.weekOfferService = weekOfferService;
        this.announcementService = announcementService;
    }

    @GetMapping
    public String getHomePage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        Announcement announcement = this.announcementService.findLast();

        if (weekOfferService.findByWeekOfferStatus(WeekOfferStatus.ACTIVE) != null) {
            WeekOffer weekOffer = weekOfferService.findByWeekOfferStatus(WeekOfferStatus.ACTIVE);
            List<Product> products = weekOffer.getProductList();
            model.addAttribute("products", products);
        }

        model.addAttribute("announcement", announcement);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "home");
        return "template";
    }

    @GetMapping("/successful-order")
    private String getSuccessfulOrderPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "success-order-page");
        return "template";
    }
}
