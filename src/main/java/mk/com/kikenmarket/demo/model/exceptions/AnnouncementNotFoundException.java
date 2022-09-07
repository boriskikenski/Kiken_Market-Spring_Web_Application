package mk.com.kikenmarket.demo.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AnnouncementNotFoundException extends RuntimeException{
    public AnnouncementNotFoundException(Long id){
        super(String.format("Announcement with id: %d is not found", id));
    }
}
