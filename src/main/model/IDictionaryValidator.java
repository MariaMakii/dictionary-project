package main.model;

import main.enums.DictionaryType;

public interface IDictionaryValidator {
    boolean validateWord(DictionaryType type, String word);
}
