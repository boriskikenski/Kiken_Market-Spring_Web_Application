package mk.com.kikenmarket.demo.model;

import lombok.Data;
import mk.com.kikenmarket.demo.model.enumerations.ShoppingCartStatus;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartID;

    private double currentValue;

    @OneToOne
    private User costumer;

    @OneToMany(mappedBy = "shoppingCart")
    private List<ProductShoppingCart> productShoppingCartList;

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus shoppingCartStatus;

    public ShoppingCart(){}

    public ShoppingCart(User costumer, ShoppingCartStatus shoppingCartStatus) {
        this.currentValue = 0.0;
        this.costumer = costumer;
        this.shoppingCartStatus = shoppingCartStatus;
    }
}
