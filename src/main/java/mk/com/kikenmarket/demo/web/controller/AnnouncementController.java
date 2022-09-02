package mk.com.kikenmarket.demo.web.controller;

import mk.com.kikenmarket.demo.model.Announcement;
import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.service.AnnouncementService;
import mk.com.kikenmarket.demo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/edit-announcement/{id}")
    private String getEditAnnouncementPage(Model model,
                                           @PathVariable Long id){
        List<Category> categories = this.categoryService.listAllCategories();
        Announcement announcement = this.announcementService.findByID(id);

        model.addAttribute("announcement", announcement);
        model.addAttribute("categories", categories);
        model.addAttribute("bodyContent", "add-announcement");
        return "template";
    }

    @PostMapping("/add-or-edit-announcement")
    private String saveAnnouncement(@RequestParam String title,
                                    @RequestParam String information,
                                    @RequestParam(required = false) Long announcementID){
        LocalDate today = LocalDate.now();
        this.announcementService.saveAnnouncement(title, information, today, announcementID);
        return "redirect:/announcements";
    }

    @PostMapping("/delete/{id}")
    private String deleteAnnouncement(@PathVariable Long id){
        this.announcementService.deleteAnnouncement(id);
        return "redirect:/announcements";
    }
}
