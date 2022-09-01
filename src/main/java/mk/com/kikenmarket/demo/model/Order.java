package mk.com.kikenmarket.demo.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "all_orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    private int numberOfReorders;

    private double moneyValue;

    private String email;

    private String toStreet;

    private Long toStreetNumber;

    private String toCity;

    private Long toBuildingEntryNumber;

    private Long toApartmentNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfOrder;

    @ManyToOne
    private User costumer;

    @OneToOne
    private ShoppingCart cart;

    public Order(){}

    public Order(User costumer, LocalDate dateOfOrder,
                 String email, String toStreet,
                 Long toStreetNumber, String toCity,
                 ShoppingCart cart , double moneyValue) {
        this.numberOfReorders = 0;
        this.moneyValue = moneyValue;
        this.email = email;
        this.toStreet = toStreet;
        this.toStreetNumber = toStreetNumber;
        this.toCity = toCity;
        this.dateOfOrder = dateOfOrder;
        this.costumer = costumer;
        this.cart = cart;
    }
}
