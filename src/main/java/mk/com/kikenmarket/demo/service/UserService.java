package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Role;
import mk.com.kikenmarket.demo.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;

public interface UserService extends UserDetailsService {
    User register (String username,
                   String password,
                   String repeatPassword,
                   String name,
                   String surname,
                   String street,
                   int streetNumber,
                   String city,
                   String role);

    void processOAuthPostLogin(String username, String authorizedClientRegistrationId);

    User findByUsername(String username);

    User getCurrentUser(Authentication authentication);
}
