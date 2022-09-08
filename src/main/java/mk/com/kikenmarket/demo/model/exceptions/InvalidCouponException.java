package mk.com.kikenmarket.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidCouponException extends RuntimeException {
    public InvalidCouponException(){
        super("Invalid coupon entered.");
    }
}
