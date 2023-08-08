package main.model;

import main.enums.DictionaryType;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

import java.util.Map;

@Component("explorer")
public class DictionaryFileExplorer {
    public DictionaryFileExplorer() {
    }

    public DictionaryFileExplorer(Map<main.enums.DictionaryType, String> paths) {
        this.paths = paths;
    }

    @Resource(name = "paths")
    private Map<main.enums.DictionaryType, String> paths;

    public void setPaths(Map<main.enums.DictionaryType, String> paths) {
        this.paths = paths;
    }


    public String getDictionaryPath(DictionaryType type) {
        return paths.get(type);
    }
}
