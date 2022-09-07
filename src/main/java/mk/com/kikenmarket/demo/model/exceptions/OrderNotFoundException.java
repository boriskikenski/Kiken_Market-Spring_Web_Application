package mk.com.kikenmarket.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(Long id){
        super(String.format("Order with id: %d is not found", id));
    }
}