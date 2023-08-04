package controller;

import enums.DictionaryType;
import model.IDictionaryManager;

public class DictionaryAppController implements IDictionaryAppController {
    public IDictionaryManager dictionaryManager;

    @Override
    public void setDictionaryManager(IDictionaryManager manager) {
        this.dictionaryManager = manager;
    }

    public DictionaryAppController(IDictionaryManager dictionaryManager) {
        setDictionaryManager(dictionaryManager);
    }

    public void setDictionaryType(DictionaryType dictionaryType) {
        this.dictionaryManager.setDictionaryType(dictionaryType);
    }

    public String getDefinition(String word) {
        return dictionaryManager.getDefinition(word);
    }

    public boolean checkWordRight(String word) {
        return dictionaryManager.getValidator().validateWord(dictionaryManager.getDictionaryType(), word);
    }

    public void addWord(String word, String definition) {
        dictionaryManager.addWord(word, definition);
    }

    public void deleteWord(String word) {
        dictionaryManager.deleteWord(word);
    }

    public String getDictionary() {
        return dictionaryManager.dictionaryToString();
    }
}
