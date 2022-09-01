package mk.com.kikenmarket.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long manufacturerID;

    private String manufacturerName;

    private String countryFrom;

    @OneToMany(mappedBy = "manufacturer")
    private List<Product> productList;

    public Manufacturer(){}

    public Manufacturer(String manufacturerName, String countryFrom) {
        this.manufacturerName = manufacturerName;
        this.countryFrom = countryFrom;
    }
}
