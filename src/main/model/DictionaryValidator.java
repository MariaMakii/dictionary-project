package main.model;

import main.enums.DictionaryType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class DictionaryValidator implements  IDictionaryValidator{

    public void showValidator(){
        System.out.println(this.wordPatterns);
    }

    public DictionaryValidator() {
    }

    @Resource(name = "validatorPatterns")
    private Map<main.enums.DictionaryType, java.util.regex.Pattern> wordPatterns;


    public void setWordPatterns(Map<main.enums.DictionaryType, java.util.regex.Pattern> patternMap) {
        this.wordPatterns = patternMap;
    }

    private Pattern getPattern(DictionaryType type) {
        return wordPatterns.get(type);
    }

    public boolean validateWord(DictionaryType type, String word) {
        return getPattern(type).matcher(word).matches();
    }
}
