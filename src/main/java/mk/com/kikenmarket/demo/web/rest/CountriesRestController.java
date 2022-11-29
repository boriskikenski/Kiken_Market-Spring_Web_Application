package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.Country;
import mk.com.kikenmarket.demo.service.CountryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/countries")
public class CountriesRestController {
    private final CountryService countryService;

    public CountriesRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    private List<Country> listAllCountries(){
        return this.countryService.getCountries();
    }
}
