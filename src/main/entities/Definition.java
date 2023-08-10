package main.entities;

import javax.persistence.*;

@Entity
@Table(name = "definition")
public class Definition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String word;
    private String definition;
    @Column(name = "dictionary")
    private int dictionaryId;

    public Definition(String word, String definition, int dictionary) {
        this.word = word;
        this.definition = definition;
        this.dictionaryId = dictionary;
    }

    public Definition() {

    }

    public Integer getId() {
        return id;
    }

    public Integer getDictionaryId() {
        return dictionaryId;
    }

    public String getWord() {
        return this.word;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public String toString() {
        return this.word + " - " + this.definition;
    }
}
