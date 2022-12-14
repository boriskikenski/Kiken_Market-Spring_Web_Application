package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Announcement;

import java.time.LocalDate;
import java.util.List;

public interface AnnouncementService {
    List<Announcement> listAllAnnouncements();
    void saveAnnouncement(String title, String information, LocalDate createdOnDate, Long id);
    Announcement findLast();
    Announcement findByID(Long id);
    void deleteAnnouncement(Long id);
}
