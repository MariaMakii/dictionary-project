package model;

import enums.DictionaryType;

public interface IDictionaryManager extends IDictionary {
    String dictionaryToString();
    void setDictionaryType(DictionaryType type);
    void setValidator(DictionaryValidator validator);
    DictionaryType getDictionaryType();
    DictionaryValidator getValidator();

}