package main.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "type")
public class DictionaryType {
    @Id
    private String type;
    private Integer validator;

    public String getType() {
        return type;
    }

    public Integer getValidator() {
        return this.validator;
    }
}
