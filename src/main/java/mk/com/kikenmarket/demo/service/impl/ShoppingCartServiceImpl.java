package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.*;
import mk.com.kikenmarket.demo.model.enumerations.ShoppingCartStatus;
import mk.com.kikenmarket.demo.repository.ProductRepository;
import mk.com.kikenmarket.demo.repository.ProductShoppingCartRepository;
import mk.com.kikenmarket.demo.repository.ShoppingCartRepository;
import mk.com.kikenmarket.demo.repository.UserRepository;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductShoppingCartRepository productShoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, ProductRepository productRepository, ProductShoppingCartRepository productShoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productShoppingCartRepository = productShoppingCartRepository;
    }

    @Override
    public boolean addProductToShoppingCart(User costumer, Long productID, double quantity) {
        boolean alreadyInShoppingCart;

        if (this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE) == null){
            this.shoppingCartRepository.save(new ShoppingCart(costumer, ShoppingCartStatus.ACTIVE));
        }

        ShoppingCart shoppingCart = this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);
        Long shoppingCartID = shoppingCart.getShoppingCartID();

        ProductShoppingCartID productShoppingCartID = new ProductShoppingCartID(productID, shoppingCartID);

        if (this.productShoppingCartRepository.findProductShoppingCartById(productShoppingCartID) == null){
            ProductShoppingCart productShoppingCart = new ProductShoppingCart(productShoppingCartID);

            productShoppingCart.setShoppingCart(shoppingCart);
            productShoppingCart.setProduct(this.productRepository.findByProductID(productID));
            productShoppingCart.setQuantity(quantity);

            Product product = this.productRepository.findByProductID(productID);
            if (product.getSale() > 0){
                productShoppingCart.setBoughtOnSale(product.getSale());
                productShoppingCart.setBoughtOnPrice
                        (product.getPrice() - (product.getPrice() * (product.getSale() / 100)));
                productShoppingCart.setTotalForProduct
                        (productShoppingCart.getBoughtOnPrice() * quantity);
            } else {
                productShoppingCart.setBoughtOnSale(0.0);
                productShoppingCart.setBoughtOnPrice(product.getPrice());
                productShoppingCart.setTotalForProduct(product.getPrice() * quantity);
            }

            this.productShoppingCartRepository.save(productShoppingCart);
            alreadyInShoppingCart = false;
        } else {
            ProductShoppingCart productShoppingCart = this.productShoppingCartRepository
                    .findProductShoppingCartById(productShoppingCartID);

            productShoppingCart.setQuantity(quantity);
            productShoppingCart.setTotalForProduct
                    (productShoppingCart.getBoughtOnPrice() * quantity);
            this.productShoppingCartRepository.save(productShoppingCart);
            alreadyInShoppingCart = true;
        }

        this.shoppingCartRepository.save(shoppingCart);
        return alreadyInShoppingCart;
    }

    @Override
    public List<ProductShoppingCart> findAllByShoppingCart(ShoppingCart shoppingCart) {
        return this.productShoppingCartRepository.findAllByShoppingCart(shoppingCart);
    }

    @Override
    public ShoppingCart findByCustomer(User costumer) {
        if (this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE) == null){
            this.shoppingCartRepository.save(new ShoppingCart(costumer, ShoppingCartStatus.ACTIVE));
        }

        return this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);
    }

    @Override
    public double getCurrenValue(User costumer, List<ProductShoppingCart> productShoppingCart) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);

        double value = productShoppingCart.stream()
                .mapToDouble(product-> product.getTotalForProduct())
                .sum();

        shoppingCart.setCurrentValue(value);
        this.shoppingCartRepository.save(shoppingCart);

        return value;
    }

    @Override
    public void deleteFromShoppingCart(Long productID, User costumer) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);
        ProductShoppingCartID productShoppingCartID =
                new ProductShoppingCartID(productID, shoppingCart.getShoppingCartID());
        this.productShoppingCartRepository.deleteById(productShoppingCartID);
    }

    @Override
    public void updateProductShoppingCart() {
        List<ShoppingCart> shoppingCarts = this.shoppingCartRepository
                .findAllByShoppingCartStatus(ShoppingCartStatus.ACTIVE);
        shoppingCarts.forEach(shoppingCart -> {
            List<ProductShoppingCart> productShoppingCartList = this.productShoppingCartRepository
                    .findAllByShoppingCart(shoppingCart);
            productShoppingCartList.forEach(productShoppingCart -> {
                Long productID = productShoppingCart.getProduct().getProductID();
                Product product = this.productRepository.findByProductID(productID);
                if (product.getSale() == 0) {
                    productShoppingCart.setBoughtOnSale(0.0);
                    productShoppingCart.setBoughtOnPrice(product.getPrice());
                } else {
                    productShoppingCart.setBoughtOnSale(product.getSale());
                    productShoppingCart.setBoughtOnPrice
                            (product.getPrice() - (product.getPrice() * (product.getSale() / 100)));
                }
                productShoppingCart.setTotalForProduct
                        (productShoppingCart.getQuantity() * productShoppingCart.getBoughtOnPrice());
                this.productShoppingCartRepository.save(productShoppingCart);
            });
        });
    }
}
