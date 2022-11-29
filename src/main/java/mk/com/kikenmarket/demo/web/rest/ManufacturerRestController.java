package mk.com.kikenmarket.demo.web.rest;

import mk.com.kikenmarket.demo.model.Manufacturer;
import mk.com.kikenmarket.demo.service.ManufacturerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/manufacturers")
public class ManufacturerRestController {
    public ManufacturerRestController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    private final ManufacturerService manufacturerService;

    @GetMapping
    private List<Manufacturer> listAllManufacturers(){
        return this.manufacturerService.listAllManufacturers();
    }

    @GetMapping("/find/{id}")
    private ResponseEntity<Manufacturer> findManufacturerByID(@PathVariable Long id){
        Manufacturer manufacturer = manufacturerService.findByID(id);
        if (manufacturer != null){
            return ResponseEntity.ok().body(manufacturer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    private ResponseEntity<Manufacturer> addManufacturer(@RequestParam String name,
                                                         @RequestParam String country){
        manufacturerService.saveManufacturer(name, country, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit/{id}")
    private ResponseEntity<Manufacturer> editManufacturer(@RequestParam String name,
                                                          @RequestParam String country,
                                                          @PathVariable Long id){
        manufacturerService.saveManufacturer(name, country, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{id}")
    private ResponseEntity<Manufacturer> deleteManufacturer(@PathVariable Long id){
        if (this.manufacturerService.findByID(id) != null){
            this.manufacturerService.deleteManufacturer(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
