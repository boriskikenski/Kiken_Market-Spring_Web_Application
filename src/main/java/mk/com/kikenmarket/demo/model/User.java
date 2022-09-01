package mk.com.kikenmarket.demo.model;

import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String name;

    private String surname;

    private String street;

    private String city;

    private int streetNumber;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;


    @Nullable
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public User(){}

    public User(String name, String surname, String street, String city,
                int streetNumber, String username, String password,
                Role role) {
        this.name = name;
        this.surname = surname;
        this.street = street;
        this.city = city;
        this.streetNumber = streetNumber;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.role = role;
    }

    public User(String username, Provider provider, Role role) {
        this.username = username;
        this.provider = provider;
        this.role = role;
    }
}
