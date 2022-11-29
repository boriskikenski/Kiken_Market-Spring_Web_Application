package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;
import mk.com.kikenmarket.demo.service.WeekOfferService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/week-offer")
public class WeekOfferRestController {
    private final WeekOfferService weekOfferService;

    public WeekOfferRestController(WeekOfferService weekOfferService) {
        this.weekOfferService = weekOfferService;
    }

    @GetMapping
    private List<Product> getWeekOfferProductList(WeekOfferStatus weekOfferStatus) {
        return weekOfferService.findByWeekOfferStatus(weekOfferStatus).getProductList();
    }
}
