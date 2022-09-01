package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Announcement;
import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.service.AnnouncementService;
import mk.com.kikenmarket.demo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final CategoryService categoryService;

    public AnnouncementController(AnnouncementService announcementService, CategoryService categoryService) {
        this.announcementService = announcementService;
        this.categoryService = categoryService;
    }

    @GetMapping
    private String getAnnouncementPage(Model model){
        List<Announcement> announcements = this.announcementService.listAllAnnouncements();
        List<Category> categories = this.categoryService.listAllCategories();

        model.addAttribute("announcements", announcements);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "announcements");
        return "template";
    }

    @GetMapping("/add-announcement")
    private String getAddAnnouncementPage(Model model){
        List<Category> categories = this.categoryService.listAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "add-announcement");
        return "template";
    }

    @PostMapping("/add-announcement")
    private String saveAnnouncement(@RequestParam String title,
                                    @RequestParam String information){
        LocalDate today = LocalDate.now();
        this.announcementService.saveAnnouncement(title, information, today);
        return "redirect:/announcements";
    }
}
