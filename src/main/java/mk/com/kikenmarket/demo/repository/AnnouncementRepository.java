package mk.com.kikenmarket.demo.repository;

import mk.com.kikenmarket.demo.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Announcement findByAnnouncementID(Long id);
}
