package model;

import enums.DictionaryType;

import java.util.Map;

public class DictionaryFileExplorer {

    private final Map<DictionaryType, String> paths;

    public DictionaryFileExplorer(Map<DictionaryType, String> paths) {
        this.paths = paths;
    }

    public String getDictionaryPath(DictionaryType type) {
        return paths.get(type);
    }
}
