package mk.com.kikenmarket.demo.model;

import lombok.Data;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class WeekOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekOfferID;

    private LocalDate createdOnDate;

    @Enumerated(EnumType.STRING)
    private WeekOfferStatus offerStatus;

    @ManyToMany
    private List<Product> productList;

    @ManyToMany
    private List<User> sendToUsers;

    @ManyToOne
    private User createdFromUser;

    public WeekOffer(){}

    public WeekOffer(WeekOfferStatus offerStatus, List<Product> productList) {
        this.offerStatus = offerStatus;
        this.productList = productList;
    }
}
