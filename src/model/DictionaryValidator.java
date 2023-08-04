package model;

import enums.DictionaryType;

import java.util.Map;
import java.util.regex.Pattern;

public class DictionaryValidator implements  IDictionaryValidator{

    public DictionaryValidator(Map<DictionaryType, Pattern> wordPatterns) {
        setWordPatterns(wordPatterns);
    }

    private Map<DictionaryType, Pattern> wordPatterns;

    public void setWordPatterns(Map<DictionaryType, Pattern> patternMap) {
        this.wordPatterns = patternMap;
    }

    private Pattern getPattern(DictionaryType type) {
        return wordPatterns.get(type);
    }

    public boolean validateWord(DictionaryType type, String word) {
        return getPattern(type).matcher(word).matches();
    }
}
