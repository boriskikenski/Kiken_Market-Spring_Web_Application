package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.ProductShoppingCart;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.ShoppingCartStatus;
import mk.com.kikenmarket.demo.repository.OrderRepository;
import mk.com.kikenmarket.demo.repository.ShoppingCartRepository;
import mk.com.kikenmarket.demo.service.OrderService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private JavaMailSender mailSender;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;


    public OrderServiceImpl(JavaMailSender mailSender, OrderRepository orderRepository,
                            ShoppingCartRepository shoppingCartRepository, ShoppingCartService shoppingCartService) {
        this.mailSender = mailSender;
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public void createOrder(User costumer, LocalDate dateOfOrder, String email,
                            String street, Long streetNumber, String city,
                            Long entryNumber, Long apartmentNumber, String mailMessage) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);

        this.orderRepository.save(new Order(costumer, dateOfOrder, email, street, streetNumber,
                city, shoppingCart, shoppingCart.getCurrentValue()));
        Order order = this.orderRepository.findByCart(shoppingCart);
        if (entryNumber != null){
            order.setToBuildingEntryNumber(entryNumber);
            this.orderRepository.save(order);
        }
        if (apartmentNumber != null){
            order.setToApartmentNumber(apartmentNumber);
            this.orderRepository.save(order);
        }

        try {
            String from = "kiken.market.order@gmail.com";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setSubject("This is an HTML email");
            helper.setFrom(from);
            helper.setTo(email);

            helper.setText(mailMessage);

            mailSender.send(message);
        } catch (MessagingException exception){
            exception.getMessage();
        }

        shoppingCart.setShoppingCartStatus(ShoppingCartStatus.ORDERED);
        this.shoppingCartRepository.save(shoppingCart);
        this.shoppingCartRepository.save(new ShoppingCart(costumer, ShoppingCartStatus.ACTIVE));

    }

    @Override
    public String generateMail(User currentCostumer) {
        ShoppingCart shoppingCart =  this.shoppingCartService.findByCustomer(currentCostumer);
        List<ProductShoppingCart> products = this.shoppingCartService
                .findAllByShoppingCart(shoppingCart);
        double total = this.shoppingCartService.getCurrenValue(currentCostumer, products);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Your order is successfully processed!\n\nYou ordered:\n"));
        stringBuilder.append(String.format("Product\tQuantity\tPrice\tTotal\n"));
        products.forEach(productShoppingCart -> {
            stringBuilder.append(String.format("%-15s\t%d\t%.2f\t%.2f\n",
                    productShoppingCart.getProduct().getName(),
                    (int)productShoppingCart.getQuantity(),
                    productShoppingCart.getBoughtOnPrice(),
                    productShoppingCart.getTotalForProduct()));
        });
        stringBuilder.append(String.format("\nTotal bill: %.2f\n\n", total));
        stringBuilder.append(String.format("Thank you for choosing us!"));
        return stringBuilder.toString();
    }
}









