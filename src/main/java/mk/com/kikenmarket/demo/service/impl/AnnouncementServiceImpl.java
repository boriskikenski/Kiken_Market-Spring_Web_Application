package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Announcement;
import mk.com.kikenmarket.demo.model.exceptions.AnnouncementNotFoundException;
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
    public void saveAnnouncement(String title, String information, LocalDate createdOnDate, Long id) {
        if (id == null){
            this.announcementRepository.save(new Announcement(title, information, createdOnDate));
        } else {
            Announcement announcement = this.announcementRepository.findByAnnouncementID(id)
                    .orElseThrow(()-> new AnnouncementNotFoundException(id));;
            announcement.setTitle(title);
            announcement.setInformation(information);
            this.announcementRepository.save(announcement);
        }
    }

    @Override
    public Announcement findLast() {
        List<Announcement> annonuncements = this.announcementRepository.findAll();
        Long annonuncementID = annonuncements.stream()
                .mapToLong(announcement -> announcement.getAnnouncementID())
                .max().getAsLong();
        return this.announcementRepository.findByAnnouncementID(annonuncementID)
                .orElseThrow(()-> new AnnouncementNotFoundException(annonuncementID));
    }

    @Override
    public Announcement findByID(Long id) {
        return this.announcementRepository.findByAnnouncementID(id)
                .orElseThrow(()-> new AnnouncementNotFoundException(id));
    }

    @Override
    public void deleteAnnouncement(Long id) {
        this.announcementRepository.deleteById(id);
    }
}
