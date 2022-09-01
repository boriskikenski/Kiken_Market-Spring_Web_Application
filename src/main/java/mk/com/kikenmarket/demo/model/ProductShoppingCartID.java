package mk.com.kikenmarket.demo.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductShoppingCartID implements Serializable {
    private Long productID;
    private Long shoppingCartID;

    public ProductShoppingCartID(Long productID, Long shoppingCartID) {
        this.productID = productID;
        this.shoppingCartID = shoppingCartID;
    }

    public ProductShoppingCartID(){}

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Long getShoppingCartID() {
        return shoppingCartID;
    }

    public void setShoppingCartID(Long shoppingCartID) {
        this.shoppingCartID = shoppingCartID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductShoppingCartID that = (ProductShoppingCartID) o;
        return productID.equals(that.productID) && shoppingCartID.equals(that.shoppingCartID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, shoppingCartID);
    }
}
