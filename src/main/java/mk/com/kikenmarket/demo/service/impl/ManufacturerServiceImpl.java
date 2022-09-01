package mk.com.kikenmarket.demo.service.impl;

import mk.com.kikenmarket.demo.model.Manufacturer;
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
    public void saveManufacturer(String name, String countryFrom) {
        manufacturerRepository.save(new Manufacturer(name, countryFrom));
    }

    @Override
    public Manufacturer findByID(Long id) {
        return manufacturerRepository.findByManufacturerID(id);
    }
}
