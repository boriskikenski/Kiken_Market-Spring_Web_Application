package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.model.WeekOffer;
import mk.com.kikenmarket.demo.model.enumerations.WeekOfferStatus;
import mk.com.kikenmarket.demo.repository.ProductRepository;
import mk.com.kikenmarket.demo.repository.WeekOfferRepository;
import mk.com.kikenmarket.demo.service.WeekOfferService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeekOfferServiceImpl implements WeekOfferService {
    private final WeekOfferRepository weekOfferRepository;
    private final ProductRepository productRepository;

    public WeekOfferServiceImpl(WeekOfferRepository weekOfferRepository, ProductRepository productRepository) {
        this.weekOfferRepository = weekOfferRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void save(List<Product> selectedProducts) {
        this.weekOfferRepository.findAll().forEach(weekOffer -> {
            if(weekOffer.getOfferStatus() == WeekOfferStatus.ACTIVE){
                List<Product> weekOfferProductList = weekOffer.getProductList();
                weekOfferProductList.forEach(product -> {
                    product.setSale(0.0);
                    this.productRepository.save(product);
                });
            }
            weekOffer.setOfferStatus(WeekOfferStatus.EXPIRED);
            this.weekOfferRepository.save(weekOffer);
        });

        this.weekOfferRepository.save(new WeekOffer(WeekOfferStatus.CREATING, selectedProducts));
    }

    @Override
    public WeekOffer findByWeekOfferStatus(WeekOfferStatus offerStatus) {
        return this.weekOfferRepository.findByOfferStatus(offerStatus);
    }

    @Override
    public void setProductSale(List<Double> sale) {
        WeekOffer weekOffer = this.weekOfferRepository.findByOfferStatus(WeekOfferStatus.CREATING);
        List<Product> products = weekOffer.getProductList();
        for (int i = 0; i < products.size(); i++) {
            Product p = this.productRepository.findByProductID(products.get(i).getProductID());
            p.setSale(sale.get(i));
            this.productRepository.save(p);
        }
        weekOffer.setOfferStatus(WeekOfferStatus.ACTIVE);
        this.weekOfferRepository.save(weekOffer);
    }
}
