package main.model;

import main.entities.Definition;
import main.entities.DefinitionDAO;
import main.enums.DictionaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component("DBManager")
@Profile("DB")
public class DictionaryDBManager implements IDictionaryManager {

    private final DefinitionDAO dao = new DefinitionDAO();
    private DictionaryShell dictionary;
    private DictionaryValidator validator;

    @Autowired
    public DictionaryDBManager(DictionaryValidator validator) {
        setValidator(validator);
    }

    @Override
    public Map<String, String> getDictionary() {
        List<Definition> definitions = dao.getAll(dictionary.getType());
        Map<String, String> result = new HashMap<>();
        definitions.forEach(definition -> {
            result.put(definition.getWord(), definition.getDefinition());
        });
        return result;
    }

    @Override
    public void deleteWord(String word) {
        List<Definition> definitions = dao.getAll(dictionary.getType());
        List<Definition> definitionsForDelete = definitions.stream().filter(definition -> definition.getWord().equals(word)).collect(Collectors.toList());
        definitionsForDelete.forEach(dao::delete);
    }

    @Override
    public String getDefinition(String word) {
        List<Definition> definitions = dao.getAll(dictionary.getType());
        List<Definition> foundedDefinitions = definitions.stream().filter(definition ->
                definition.getWord().equals(word)
        ).collect(Collectors.toList());

        StringJoiner joiner = new StringJoiner(",");
        foundedDefinitions.forEach(definition -> {
            joiner.add(definition.getDefinition());
        });

        return joiner.toString();
    }

    @Override
    public void addWord(String word, String definition) {
        if (validator.validateWord(dictionary.getType(), word)) {
            Definition def = new Definition(word, definition, dictionary.getId());
            dao.save(def);
        }
    }

    @Override
    public String dictionaryToString() {
        List<Definition> definitions = dao.getAll(dictionary.getType());
        StringJoiner joiner = new StringJoiner("\n");
        definitions.forEach(definition -> {
            joiner.add(definition.getWord() + " - " + definition.getDefinition());
        });

        return joiner.toString();
    }

    @Override
    public void setValidator(DictionaryValidator validator) {
        this.validator = validator;
    }

    @Override
    public DictionaryType getDictionaryType() {
        return dictionary.getType();
    }

    @Override
    public DictionaryValidator getValidator() {
        return this.validator;
    }

    @Override
    public void setDictionary(DictionaryShell dictionary) {
        this.dictionary = dictionary;
    }
}
