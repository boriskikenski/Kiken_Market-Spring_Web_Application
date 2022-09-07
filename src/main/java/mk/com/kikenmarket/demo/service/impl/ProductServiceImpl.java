package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Category;
import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.Product;
import mk.com.kikenmarket.demo.model.exceptions.CategoryNotFoundException;
import mk.com.kikenmarket.demo.model.exceptions.ManufacturerNotFoundException;
import mk.com.kikenmarket.demo.model.exceptions.ProductNotFoundException;
import mk.com.kikenmarket.demo.repository.CategoryRepository;
import mk.com.kikenmarket.demo.repository.ManufacturerRepository;
import mk.com.kikenmarket.demo.repository.ProductRepository;
import mk.com.kikenmarket.demo.service.ProductService;
import mk.com.kikenmarket.demo.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ShoppingCartService shoppingCartService;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ShoppingCartService shoppingCartService, ManufacturerRepository manufacturerRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.shoppingCartService = shoppingCartService;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void saveProduct(Long productID, String name, int quantity, double price,
                            Manufacturer manufacturer, Category categories,
                            Date expirationDate, String productImageURL) {
        if (productID == null){
            this.productRepository.save(new Product(name, quantity, price, manufacturer, categories,
                    expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    productImageURL));
        } else  {
            Product product = this.productRepository.findByProductID(productID)
                    .orElseThrow(() -> new ProductNotFoundException(productID));
            product.setName(name);
            product.setQuantity(quantity);
            product.setPrice(price);
            product.setManufacturer(manufacturer);
            product.setCategories(categories);
            product.setExpirationDate(expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            product.setProductImageURL(productImageURL);
            this.productRepository.save(product);
            this.shoppingCartService.updateProductShoppingCart();
        }
    }

    @Override
    public List<Product> listAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Product> findAllByID(List<Long> listOfProductIDs) {
        List<Product> selectedProducts = listOfProductIDs.stream()
                .map(productID -> this.productRepository.findByProductID(productID).get())
                .collect(Collectors.toList());
        return selectedProducts;
    }

    @Override
    public Product findByID(Long id) {
        return this.productRepository.findByProductID(id)
                .orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        this.productRepository.deleteProductByProductID(id);
    }

    @Override
    public List<Product> listAllProductsFiltered(Long manufacturerID, Long categoryID) {
        List<Product> products = new ArrayList<>();

        if (manufacturerID == null && categoryID == null){
            products = listAllProducts();
        } else if (manufacturerID == -1 && categoryID != null) {
            products = this.productRepository.findAllByCategories
                    (this.categoryRepository.findByCategoryID(categoryID)
                            .orElseThrow(()-> new CategoryNotFoundException(categoryID)));
        } else if (manufacturerID != null && categoryID == -1) {
            products = this.productRepository.findAllByManufacturer
                    (this.manufacturerRepository.findByManufacturerID(manufacturerID)
                            .orElseThrow(()->new ManufacturerNotFoundException(manufacturerID)));
        } else if (manufacturerID != null && categoryID != null) {
            products = this.productRepository.findAllByManufacturerAndCategories
                    (this.manufacturerRepository.findByManufacturerID(manufacturerID)
                                    .orElseThrow(()->new ManufacturerNotFoundException(manufacturerID)),
                            this.categoryRepository.findByCategoryID(categoryID)
                                    .orElseThrow(()-> new CategoryNotFoundException(categoryID)));
        }

        return products;
    }

    @Override
    public List<Product> search(String search) {
        List<Product> allProducts = listAllProducts();
        List<Product> foundProducts = new ArrayList<>();

        for (int i = 0; i < allProducts.size(); i++){
            Product product = allProducts.get(i);
            List<String> keyWord = product.getCategories().getKeyWords();

            if (product.getName().toLowerCase().equals(search.toLowerCase())){
                foundProducts.add(product);
            }
            if (product.getCategories().getName().toLowerCase().equals(search.toLowerCase())){
                foundProducts.add(product);
            }
            if (product.getManufacturer().getManufacturerName().toLowerCase().equals(search.toLowerCase())){
                foundProducts.add(product);
            }
            for (int j = 0; j < keyWord.size(); j++){
                if (keyWord.get(j).toLowerCase().equals(search.toLowerCase())){
                    foundProducts.add(product);
                }
            }
        }

        return foundProducts;
    }
}
