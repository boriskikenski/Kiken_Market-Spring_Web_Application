package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.Announcement;
import mk.com.kikenmarket.demo.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/announcements")
public class AnnouncementRestController {
    private final AnnouncementService announcementService;

    public AnnouncementRestController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    private List<Announcement> listAllAnnouncements(){
        return this.announcementService.listAllAnnouncements();
    }

    @GetMapping("/find/{id}")
    private ResponseEntity<Announcement> getAnnouncementByID(@PathVariable Long id){
        Announcement announcement = this.announcementService.findByID(id);
        if (announcement != null){
            return ResponseEntity.ok().body(announcement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find-last")
    private ResponseEntity<Announcement> findLastAnnouncement(){
        Announcement announcement = this.announcementService.findLast();
        if (announcement != null){
            return ResponseEntity.ok().body(announcement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    private ResponseEntity<Announcement> addAnnouncement(@RequestParam String title,
                                                          @RequestParam String information){
        LocalDate today = LocalDate.now();
        this.announcementService.saveAnnouncement(title, information, today, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/{id}")
    private ResponseEntity<Announcement> editAnnouncement(@PathVariable Long id,
                                                          @RequestParam String title,
                                                          @RequestParam String information){
        LocalDate today = LocalDate.now();
        this.announcementService.saveAnnouncement(title, information, today, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{id}")
    private ResponseEntity<Announcement> deleteAnnouncement(@PathVariable Long id){
        if (this.announcementService.findByID(id) != null){
            this.announcementService.deleteAnnouncement(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
