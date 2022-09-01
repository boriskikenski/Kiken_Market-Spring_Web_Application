package mk.com.kikenmarket.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    private String name;

    private String description;

    @ElementCollection
    private List<String> keyWords;

    public Category(){}

    public Category(String name, String description, List<String> keyWords) {
        this.name = name;
        this.description = description;
        this.keyWords = keyWords;
    }
}
