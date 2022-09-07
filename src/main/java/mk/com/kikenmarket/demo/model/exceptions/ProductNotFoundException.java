package mk.com.kikenmarket.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(Long id){
        super(String.format("Product with id: %d is not found", id));
    }
}
