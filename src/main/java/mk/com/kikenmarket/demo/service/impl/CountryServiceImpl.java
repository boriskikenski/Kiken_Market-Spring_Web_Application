package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Country;
import mk.com.kikenmarket.demo.service.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    @Override
    public List<Country> getCountries() {
        String url = "https://restcountries.com/v3.1/all";
        RestTemplate restTemplate = new RestTemplate();

        Country[] countries = restTemplate.getForObject(url, Country[].class);
        List<Country> countryList = new ArrayList<>();
        for (Country c : countries) {
            countryList.add(c);
        }
        Comparator<Country> comparator = Comparator.comparing(h -> h.getName().getCommon());
        countryList.sort(comparator);

        return countryList;
    }
}
