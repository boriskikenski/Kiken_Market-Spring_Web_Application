package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Coupon;
import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.User;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Long createOrder(User costumer,
                     LocalDate dateOfOrder,
                     String email,
                     String street,
                     Long streetNumber,
                     String city,
                     Long entryNumber,
                     Long apartmentNumber,
                     Coupon coupon);

    String generateMail(Long orderID);

    List<Order> listAllOrdersForCostumer(User costumer);

    void reorder(Long id, User costumer);

    Long checkForReorder(User costumer);

    void sendOrderMail(Long orderID, String mailMessage);
}
