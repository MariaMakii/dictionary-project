import controller.DictionaryAppController;
import model.*;
import enums.DictionaryType;
import view.DictionaryApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String firstPath = "C:/untitled/files/dictionary1.txt";
        String secondPath = "C:/untitled/files/dictionary2.txt";

        ArrayList<DictionaryFile> loadedDictionaries = new ArrayList<>();
        loadedDictionaries.add(new DictionaryFile(DictionaryType.FIRST, firstPath));
        loadedDictionaries.add(new DictionaryFile(DictionaryType.SECOND, secondPath));

        Map<DictionaryType, String> filePaths = new HashMap<>();
        filePaths.put(DictionaryType.FIRST, firstPath);
        filePaths.put(DictionaryType.SECOND, secondPath);

        Pattern firstTypePattern = Pattern.compile("[a-zA-Z]{4}");
        Pattern secondTypePattern = Pattern.compile("\\d{5}");

        Map<DictionaryType, Pattern> patternsMap = new HashMap<>();
        patternsMap.put(DictionaryType.FIRST, firstTypePattern);
        patternsMap.put(DictionaryType.SECOND, secondTypePattern);

        DictionaryValidator validator = new DictionaryValidator(patternsMap);
        DictionaryFileExplorer explorer = new DictionaryFileExplorer(filePaths);
        IDictionaryManager manager = new DictionaryFileManager(explorer, validator);
        DictionaryAppController controller = new DictionaryAppController(manager);

        DictionaryApp app = new DictionaryApp(controller, loadedDictionaries);
        app.run();
    }
}
