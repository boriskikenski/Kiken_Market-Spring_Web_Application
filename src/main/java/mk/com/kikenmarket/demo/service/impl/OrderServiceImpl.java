package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.*;
import mk.com.kikenmarket.demo.model.enumerations.CouponStatus;
import mk.com.kikenmarket.demo.model.enumerations.ShoppingCartStatus;
import mk.com.kikenmarket.demo.model.exceptions.OrderNotFoundException;
import mk.com.kikenmarket.demo.repository.CouponRepository;
import mk.com.kikenmarket.demo.repository.OrderRepository;
import mk.com.kikenmarket.demo.repository.ShoppingCartRepository;
import mk.com.kikenmarket.demo.service.CouponService;
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
    private final CouponService couponService;
    private final CouponRepository couponRepository;


    public OrderServiceImpl(JavaMailSender mailSender, OrderRepository orderRepository,
                            ShoppingCartRepository shoppingCartRepository,
                            ShoppingCartService shoppingCartService, CouponService couponService,
                            CouponRepository couponRepository) {
        this.mailSender = mailSender;
        this.orderRepository = orderRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartService = shoppingCartService;
        this.couponService = couponService;
        this.couponRepository = couponRepository;
    }

    @Override
    public Long createOrder(User costumer, LocalDate dateOfOrder, String email,
                            String street, Long streetNumber, String city,
                            Long entryNumber, Long apartmentNumber, Coupon coupon) {
        Long orderID = null;

        ShoppingCart shoppingCart = this.shoppingCartRepository.findShoppingCartByCostumerAndShoppingCartStatus
                (costumer, ShoppingCartStatus.ACTIVE);

        if (checkForReorder(costumer) == null){
            double orderValue = shoppingCart.getCurrentValue() - (shoppingCart.getCurrentValue() * (coupon.getSalePercentage()/100));
            this.orderRepository.save(new Order(costumer, dateOfOrder, email, street, streetNumber,
                    city, shoppingCart, orderValue));
            Order order = this.orderRepository.findByCart(shoppingCart);
            orderID = order.getOrderID();
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
            double orderValue = shoppingCart.getCurrentValue() - (shoppingCart.getCurrentValue() * (coupon.getSalePercentage()/100));
            orderID = reorder.getOrderID();
            reorder.setNumberOfReorders(reorder.getNumberOfReorders()+1);
            reorder.setMoneyValue(orderValue);
            reorder.setDateOfOrder(dateOfOrder);
            this.orderRepository.save(reorder);
        }

        shoppingCart.setShoppingCartStatus(ShoppingCartStatus.ORDERED);
        this.shoppingCartRepository.save(shoppingCart);
        this.shoppingCartRepository.save(new ShoppingCart(costumer, ShoppingCartStatus.ACTIVE));

        coupon.setStatus(CouponStatus.USED);
        this.couponRepository.save(coupon);

        return orderID;
    }

    @Override
    public String generateMail(Long orderID) {
        Order order = this.orderRepository.findByOrderID(orderID)
                .orElseThrow(()->new OrderNotFoundException(orderID));
        ShoppingCart shoppingCart = order.getCart();
        List<ProductShoppingCart> products = this.shoppingCartService
                .findAllByShoppingCart(shoppingCart);
        double total = order.getMoneyValue();
        Coupon coupon = this.couponService.findLastForCostumer(order.getCostumer());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Your order is successfully processed!\n\nYou ordered:\n"));
        stringBuilder.append(String.format("Product\t\tQuantity\t\tPrice\t\tTotal\n"));
        products.forEach(productShoppingCart -> {
            stringBuilder.append(String.format("%-15s\t\t%d\t\t%.2f\t\t%.2f\n",
                    productShoppingCart.getProduct().getName(),
                    (int)productShoppingCart.getQuantity(),
                    productShoppingCart.getBoughtOnPrice(),
                    productShoppingCart.getTotalForProduct()));
        });
        stringBuilder.append(String.format("\nTotal bill: %.2f\n\n", total));
        if (coupon.getSalePercentage() == 0){
            this.couponRepository.delete(coupon);
        } else {
            stringBuilder.append(String.format
                    ("You earned coupon!\n" +
                            "Congratulations!\n" +
                            "Coupon is for sale of: %.1f\n"+
                            "Coupon id: %d\n\n",
                            coupon.getSalePercentage(), coupon.getCouponID()));
        }
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
}









