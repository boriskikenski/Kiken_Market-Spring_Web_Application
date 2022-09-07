package mk.com.kikenmarket.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class OutOfStockException extends RuntimeException{
    public OutOfStockException(){
        super("Asked quantity is not available.");
    }
}
