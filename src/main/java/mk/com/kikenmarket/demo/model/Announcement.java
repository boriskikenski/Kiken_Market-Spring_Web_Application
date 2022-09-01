package mk.com.kikenmarket.demo.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementID;

    private String title;

    private String information;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdOnDate;

    public Announcement(){}

    public Announcement(String title, String information, LocalDate createdOnDate) {
        this.title = title;
        this.information = information;
        this.createdOnDate = createdOnDate;
    }
}
