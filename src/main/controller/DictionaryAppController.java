package main.controller;

import main.model.DictionaryShell;
import main.model.IDictionaryManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("controller")
public class DictionaryAppController implements IDictionaryAppController {
    public IDictionaryManager dictionaryManager;

    @Override
    public void setDictionaryManager(IDictionaryManager manager) {
        this.dictionaryManager = manager;
    }

    @Autowired
    public DictionaryAppController( IDictionaryManager dictionaryManager) {
        setDictionaryManager(dictionaryManager);
    }

    public void setDictionary(DictionaryShell dictionary){
        this.dictionaryManager.setDictionary(dictionary);
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
