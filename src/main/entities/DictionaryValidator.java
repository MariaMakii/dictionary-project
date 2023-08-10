package main.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "validator")
public class DictionaryValidator {
    @Id
    private Integer id;
    private String regex;
    private Integer symbol_count;

    public String getRegex() {
        return regex;
    }

    public Integer getSymbol_count() {
        return symbol_count;
    }

    public Integer getId() {
        return id;
    }
}
