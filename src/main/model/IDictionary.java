package main.model;

import java.util.Map;

public interface IDictionary {
    Map<String, String> getDictionary();

    void deleteWord(String word);

    String getDefinition(String word);

    void addWord(String word, String definition);
}