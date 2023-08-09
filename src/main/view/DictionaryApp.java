package main.view;

import main.controller.DictionaryAppController;
import main.enums.DictionaryType;
import main.model.DictionaryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Component
public class DictionaryApp implements IDictionaryApp {
    @Autowired
    @Qualifier("controller")
    DictionaryAppController controller;

    @Autowired
    List<DictionaryFile> loadedDictionaries;

    public DictionaryApp() {
    }

    @Override
    public void setController(DictionaryAppController controller) {
        this.controller = controller;
    }

    public void setLoadedDictionaries(ArrayList<DictionaryFile> loadedDictionaries) {
        this.loadedDictionaries = loadedDictionaries;
    }

    Scanner sc = new Scanner(System.in);
    List<Integer> commands = Arrays.asList(0, 1, 2, 3, 4, 5);


    @PreDestroy
    private void closeApp() {
        sc.close();
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Integer stringToInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    String instructions = """
            Для выбора словаря №1 нажмите 1          
            Для поиска слова нажмите 2
            Для добавления слова нажмите 3
            Для удаления слова нажмите 4
            Чтобы вернуться в меню нажмите 0""";

    public boolean checkCommand(String string) {
        return isNumber(string) && string.length() == 1 && commands.contains(stringToInteger(string));
    }

    public void printInstructions() {
        System.out.println(instructions);
    }

    public void run() {
        printInstructions();
        String entered;
        while (!(entered = sc.next()).equals("esc")) {
            if (checkCommand(entered)) {
                action(stringToInteger(entered));
            } else {
                System.out.println("Неверная команда");
            }

        }
    }

    private void action(int action) {
        switch (action) {
            case 0 -> {
                printInstructions();
            }
            case 1 -> {
                selectDictionary();
            }
            case 2 -> {
                if (controller.dictionaryManager.getDictionaryType() != null) {
                    findWord();
                } else {
                    selectDictionary();
                }
            }
            case 3 -> {
                if (controller.dictionaryManager.getDictionaryType() != null) {
                    addWord();
                } else {
                    selectDictionary();
                }
            }
            case 4 -> {
                if (controller.dictionaryManager.getDictionaryType() != null) {
                    deleteWord();
                } else {
                    selectDictionary();
                }
            }
            default -> System.out.println("?");
        }
    }

    public void printDictionary(DictionaryType type) {
        System.out.println("ТЕКУЩИЙ СЛОВАРЬ №" + (type == DictionaryType.FIRST ? "1" : "2"));
        String result = controller.getDictionary();
        System.out.println(result);
    }

    public void findWord() {
        System.out.println("Введите слово, которое требуется найти:");
        String string = sc.next();
        if (!checkCommand(string)) {
            System.out.println(controller.getDefinition(string));
            System.out.println();
        }
    }

    public void addWord() {
        System.out.println("Введите слово:");
        String word = sc.next();
        if (controller.checkWordRight(word)) {
            System.out.println("Введите определение:");
            String definition = sc.next();
            if (!checkCommand(definition)) {
                controller.addWord(word, definition);
                System.out.println("Слово добавлено в словарь");
                System.out.println();
            }
        } else if (!checkCommand(word)) {
            System.out.println("Слово введено неверно");
            System.out.println();
        }
    }

    public void deleteWord() {
        System.out.println("Введите слово, которое нужно удалить:");
        String word = sc.next();
        if (!checkCommand(word)) {
            controller.deleteWord(word);
            System.out.println("Слово удалено.");
        }
    }

    private void selectDictionary() {
        for (int i = 0; i < loadedDictionaries.size(); i++) {
            System.out.println("Для выбора словаря " + loadedDictionaries.get(i).getDictionaryPath() + " введите D" + (i + 1));
        }
        Pattern dictionaryCommand = Pattern.compile("D\\d{1}");
        String command = sc.next();
        if (!checkCommand(command) && !command.equals("esc")) {

            int dictionaryNumber = stringToInteger(String.valueOf(command.charAt(1)));
            if (dictionaryCommand.matcher(command).matches()
                    && (
                    dictionaryNumber > 0 &&
                            dictionaryNumber <= loadedDictionaries.size()
            )
            ) {
                DictionaryType type = loadedDictionaries.get(dictionaryNumber - 1).getType();
                controller.setDictionaryType(type);
                printDictionary(type);
            }
        }
    }
}
