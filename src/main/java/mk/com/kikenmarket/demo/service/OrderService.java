package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Order;
import mk.com.kikenmarket.demo.model.User;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    void createOrder(User costumer,
                     LocalDate dateOfOrder,
                     String email,
                     String street,
                     Long streetNumber,
                     String city,
                     Long entryNumber,
                     Long apartmentNumber);

    String generateMail(Long orderID);

    List<Order> listAllOrdersForCostumer(User costumer);

    void reorder(Long id, User costumer);

    Long checkForReorder(User costumer);

    void sendOrderMail(Long orderID, String mailMessage);

    Long getCurrentOrderID(User costumer);
}
