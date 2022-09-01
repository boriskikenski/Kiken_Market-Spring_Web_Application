package mk.com.kikenmarket.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationID;

    private LocalDateTime localDateTime;

    private String description;

    @ManyToOne
    private User user;

    public Notification(){}
}
