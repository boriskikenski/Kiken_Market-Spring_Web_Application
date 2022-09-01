package mk.com.kikenmarket.demo.service;

import mk.com.kikenmarket.demo.model.Manufacturer;

import java.util.List;

public interface ManufacturerService {
    List<Manufacturer> listAllManufacturers();
    void saveManufacturer(String name, String countryFrom);
    Manufacturer findByID(Long id);
}
