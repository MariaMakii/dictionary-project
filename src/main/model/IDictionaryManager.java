package main.model;

import main.enums.DictionaryType;

public interface IDictionaryManager extends IDictionary {
    String dictionaryToString();

    //void setDictionaryType(DictionaryType type);

    void setValidator(DictionaryValidator validator);

    DictionaryType getDictionaryType();

    DictionaryValidator getValidator();

    void setDictionary(DictionaryShell dictionary);
}