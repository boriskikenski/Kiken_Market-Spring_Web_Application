package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.model.exceptions.ManufacturerNotFoundException;
import mk.com.kikenmarket.demo.repository.ManufacturerRepository;
import mk.com.kikenmarket.demo.service.ManufacturerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public List<Manufacturer> listAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public void saveManufacturer(String name, String countryFrom, Long id) {
        if (id == null){
            manufacturerRepository.save(new Manufacturer(name, countryFrom));
        } else {
            Manufacturer manufacturer = this.manufacturerRepository.findByManufacturerID(id)
                    .orElseThrow(()->new ManufacturerNotFoundException(id));
            manufacturer.setManufacturerName(name);
            manufacturer.setCountryFrom(countryFrom);
            this.manufacturerRepository.save(manufacturer);
        }
    }

    @Override
    public Manufacturer findByID(Long id) {
        return manufacturerRepository.findByManufacturerID(id)
                .orElseThrow(()->new ManufacturerNotFoundException(id));
    }

    @Override
    public void deleteManufacturer(Long id) {
        this.manufacturerRepository.deleteById(id);
    }
}
