package main.model;

import main.entities.Definition;
import main.entities.DefinitionDAO;
import main.enums.DictionaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component("DBManager")
public class DictionaryDBManager implements IDictionaryManager {

    private DefinitionDAO dao = new DefinitionDAO();
    private DictionaryType dictionaryType = null;
    private DictionaryValidator validator;

    @Autowired
    public DictionaryDBManager(DictionaryValidator validator) {
        setValidator(validator);
    }

    @Override
    public Map<String, String> getDictionary() {
        int dictionaryTypeInt = dictionaryType.equals(DictionaryType.FIRST) ? 0 : 1;
        List<Definition> definitions = dao.getAll(dictionaryTypeInt);
        Map<String, String> result = new HashMap<>();
        definitions.forEach(definition -> {
            result.put(definition.getWord(), definition.getDefinition());
        });
        return result;
    }

    private int getDictionaryDBType() {
        return dictionaryType.equals(DictionaryType.FIRST) ? 0 : 1;
    }

    @Override
    public void deleteWord(String word) {
        List<Definition> definitions = dao.getAll(getDictionaryDBType());
        List<Definition> definitionsForDelete = definitions.stream().filter(definition -> definition.getWord().equals(word)).collect(Collectors.toList());
        definitionsForDelete.forEach(definition -> {
            dao.delete(definition);
        });
    }

    @Override
    public String getDefinition(String word) {
        List<Definition> definitions = dao.getAll(getDictionaryDBType());
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
        if (validator.validateWord(dictionaryType, word)) {
            Definition def = new Definition(word, definition, getDictionaryDBType());
            dao.save(def);
        }
    }

    @Override
    public String dictionaryToString() {
        List<Definition> definitions = dao.getAll(getDictionaryDBType());
        StringJoiner joiner = new StringJoiner("\n");
        definitions.forEach(definition -> {
            joiner.add(definition.getWord() + " - " + definition.getDefinition());
        });

        return joiner.toString();
    }

    @Override
    public void setDictionaryType(DictionaryType type) {
        dictionaryType = type;
    }

    @Override
    public void setValidator(DictionaryValidator validator) {
        this.validator = validator;
    }

    @Override
    public DictionaryType getDictionaryType() {
        return dictionaryType;
    }

    @Override
    public DictionaryValidator getValidator() {
        return this.validator;
    }
}
