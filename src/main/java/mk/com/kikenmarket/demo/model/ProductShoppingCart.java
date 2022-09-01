package mk.com.kikenmarket.demo.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Data
@Entity
public class ProductShoppingCart {
    @EmbeddedId
    private ProductShoppingCartID id = new ProductShoppingCartID();

    @ManyToOne
    @MapsId("productID")
    private Product product;

    @ManyToOne
    @MapsId("shoppingCartID")
    private ShoppingCart shoppingCart;

    private double quantity;

    private double boughtOnSale;

    private double boughtOnPrice;

    private double totalForProduct;

    public ProductShoppingCart(){}

    public ProductShoppingCart(ProductShoppingCartID id) {
        this.id = id;
    }
}
