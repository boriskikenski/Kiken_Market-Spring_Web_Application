package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.User;

import java.time.LocalDate;

public interface OrderService {
    void createOrder(User costumer,
                     LocalDate dateOfOrder,
                     String email,
                     String street,
                     Long streetNumber,
                     String city,
                     Long entryNumber,
                     Long apartmentNumber,
                     String mailMessage);

    String generateMail(User currentCostumer);
}
