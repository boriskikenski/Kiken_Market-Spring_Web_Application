package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.CustomOAuth2User;
import mk.com.kikenmarket.demo.model.Provider;
import mk.com.kikenmarket.demo.model.Role;
import mk.com.kikenmarket.demo.model.User;
import mk.com.kikenmarket.demo.model.exceptions.InvalidUsernameOrPasswordException;
import mk.com.kikenmarket.demo.model.exceptions.PasswordsDoNotMatchException;
import mk.com.kikenmarket.demo.model.exceptions.UsernameAlreadyExistsException;
import mk.com.kikenmarket.demo.repository.UserCrudRepository;
import mk.com.kikenmarket.demo.repository.UserRepository;
import mk.com.kikenmarket.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCrudRepository userCrudRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserCrudRepository userCrudRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public User register(String username, String password, String repeatPassword,
                         String name, String surname, String street, int streetNumber,
                         String city, String role) {
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        return userRepository.save(new User(name,surname, street,
                city, streetNumber,username,passwordEncoder.encode(password),Role.valueOf(role)));
    }

    @Override
    public void processOAuthPostLogin(String username, String authorizedClientRegistrationId) {
        User existUser = userCrudRepository.getUserByUsername(username);

        if (existUser == null && authorizedClientRegistrationId.equals("google")) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setRole(Role.ROLE_COSTUMER);

            userRepository.save(newUser);
        } else if (existUser == null && authorizedClientRegistrationId.equals("facebook")) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setProvider(Provider.FACEBOOK);
            newUser.setRole(Role.ROLE_COSTUMER);

            userRepository.save(newUser);
        }
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).get();
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        String currentUser;
        if(authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
            currentUser = user.getEmail();
        } else {
            User user = (User) authentication.getPrincipal();
            currentUser = user.getUsername();
        }
        User costumer = this.userRepository.findByUsername(currentUser).get();
        return costumer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
    }
}
