package model;

import enums.DictionaryType;

public interface IDictionaryValidator {
    boolean validateWord(DictionaryType type, String word);
}
