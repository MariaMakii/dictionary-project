package main.view;

import main.controller.DictionaryAppController;

public interface IDictionaryApp {
    void setController(DictionaryAppController controller);

    void printDictionary(String dictionaryName);

    void findWord();

    void addWord();

    void deleteWord();
}
