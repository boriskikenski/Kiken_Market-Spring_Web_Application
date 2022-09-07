package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.ProductShoppingCart;
import mk.com.kikenmarket.demo.model.ShoppingCart;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.enumerations.ShoppingCartStatus;
import mk.com.kikenmarket.demo.model.exceptions.OrderNotFoundException;
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
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
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
                            Long entryNumber, Long apartmentNumber) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);

        if (checkForReorder(costumer) == null){
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
        } else {
            Order reorder = this.orderRepository.findByOrderID(checkForReorder(costumer)).get();
            reorder.setNumberOfReorders(reorder.getNumberOfReorders()+1);
            reorder.setMoneyValue(shoppingCart.getCurrentValue());
            reorder.setDateOfOrder(dateOfOrder);
            this.orderRepository.save(reorder);
        }

        shoppingCart.setShoppingCartStatus(ShoppingCartStatus.ORDERED);
        this.shoppingCartRepository.save(shoppingCart);
        this.shoppingCartRepository.save(new ShoppingCart(costumer, ShoppingCartStatus.ACTIVE));
    }

    @Override
    public String generateMail(Long orderID) {
        Order order = this.orderRepository.findByOrderID(orderID)
                .orElseThrow(()->new OrderNotFoundException(orderID));
        ShoppingCart shoppingCart = order.getCart();
        List<ProductShoppingCart> products = this.shoppingCartService
                .findAllByShoppingCart(shoppingCart);
        double total = order.getMoneyValue();

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

    @Override
    public List<Order> listAllOrdersForCostumer(User costumer) {
        return this.orderRepository.findAllByCostumer(costumer);
    }

    @Override
    public void reorder(Long id, User costumer) {
        Order order = this.orderRepository.findByOrderID(id)
                .orElseThrow(()->new OrderNotFoundException(id));;

        order.getCart().getProductShoppingCartList().stream()
                .forEach(productShoppingCart -> {
                    this.shoppingCartService.addProductToShoppingCart
                            (costumer, productShoppingCart.getProduct().getProductID(), productShoppingCart.getQuantity());
                });
    }

    @Override
    public Long checkForReorder(User costumer) {
        Long reorder = null;
        List<Order> costumerOrders = this.orderRepository.findAllByCostumer(costumer);
        List<Long> shoppingCartProducts = this.shoppingCartRepository
                .findShoppingCartByCostumerAndShoppingCartStatus(costumer, ShoppingCartStatus.ACTIVE)
                .getProductShoppingCartList()
                .stream()
                .map(productShoppingCart -> productShoppingCart.getProduct().getProductID())
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < costumerOrders.size(); i++){
            Order order = costumerOrders.get(i);
            List<Long> orderProducts = order.getCart().getProductShoppingCartList()
                    .stream()
                    .map(productShoppingCart -> productShoppingCart.getProduct().getProductID())
                    .sorted()
                    .collect(Collectors.toList());
            if (orderProducts.equals(shoppingCartProducts))
                reorder = order.getOrderID();
        }
        return reorder;
    }

    @Override
    public void sendOrderMail(Long orderID, String mailMessage) {
        String email = this.orderRepository.findByOrderID(orderID)
                .orElseThrow(()->new OrderNotFoundException(orderID))
                .getEmail();
        try {
            String from = "kiken.market.order@gmail.com";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setSubject("ORDER");
            helper.setFrom(from);
            helper.setTo(email);

            helper.setText(mailMessage);

            mailSender.send(message);
        } catch (MessagingException exception){
            exception.getMessage();
        }
    }

    @Override
    public Long getCurrentOrderID(User costumer) {
        return this.orderRepository.findAllByCostumer(costumer)
                .stream()
                .map(order -> order.getOrderID())
                .max(Comparator.comparing(Function.identity())).get();
    }
}









