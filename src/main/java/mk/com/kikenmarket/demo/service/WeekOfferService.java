package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.model.WeekOffer;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;

import java.util.List;

public interface WeekOfferService {
    void save(List<Product> selectedProducts);

    WeekOffer findByWeekOfferStatus(WeekOfferStatus offerStatus);

    void setProductSale(List<Double> sale);
}
