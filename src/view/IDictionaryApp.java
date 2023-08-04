package view;

import controller.DictionaryAppController;
import enums.DictionaryType;

public interface IDictionaryApp {
    void setController(DictionaryAppController controller);

    void printDictionary(DictionaryType type);

    void findWord();

    void addWord();

    void deleteWord();
}
