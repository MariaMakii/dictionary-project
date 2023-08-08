package main.model;

import main.enums.DictionaryType;

public class DictionaryFile {
    private DictionaryType type;
    private String dictionaryPath;

    public String getDictionaryPath() {
        return dictionaryPath;
    }

    public DictionaryType getType() {
        return type;
    }

    public DictionaryFile(DictionaryType type, String dictionaryPath) {
        this.type = type;
        this.dictionaryPath = dictionaryPath;
    }

    public DictionaryFile() {
    }
}
