package mk.com.kikenmarket.demo.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    private String name;

    private int quantity;

    private double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    private Double sale;

    private String productImageURL;

    @ManyToOne
    private Manufacturer manufacturer;

    @ManyToOne
    private Category categories;

    @OneToMany(mappedBy = "product")
    private List<ProductShoppingCart> productShoppingCartList;

    public Product(){}

    public Product(String name, int quantity, double price,
                   Manufacturer manufacturer, Category categories,
                   LocalDate expirationDate, String productImageURL) {
        this.sale = 0.0;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.manufacturer = manufacturer;
        this.categories = categories;
        this.expirationDate = expirationDate;
        this.productImageURL = productImageURL;
    }
}
