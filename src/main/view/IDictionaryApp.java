package main.view;

import main.controller.DictionaryAppController;
import main.enums.DictionaryType;

public interface IDictionaryApp {
    void setController(DictionaryAppController controller);

    void printDictionary(DictionaryType type);

    void findWord();

    void addWord();

    void deleteWord();
}
