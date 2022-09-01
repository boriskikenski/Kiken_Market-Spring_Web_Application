package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Announcement;
import mk.com.kikenmarket.demo.repository.AnnouncementRepository;
import mk.com.kikenmarket.demo.service.AnnouncementService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<Announcement> listAllAnnouncements() {
        return this.announcementRepository.findAll();
    }

    @Override
    public void saveAnnouncement(String title, String information, LocalDate createdOnDate) {
        this.announcementRepository.save(new Announcement(title, information, createdOnDate));
    }

    @Override
    public Announcement findLast() {
        List<Announcement> annonuncements = this.announcementRepository.findAll();
        Long annonuncementID = annonuncements.stream()
                .mapToLong(announcement -> announcement.getAnnouncementID())
                .max().getAsLong();
        return this.announcementRepository.findByAnnouncementID(annonuncementID);
    }
}
