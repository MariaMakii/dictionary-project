package main.model;

import main.enums.DictionaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component("fileManager")
@Profile("file")
public class DictionaryFileManager implements IDictionaryManager {

    private DictionaryShell dictionary;
    private String separator;

    private DictionaryValidator validator;

    @Autowired
    public DictionaryFileManager(DictionaryValidator validator) {
        setValidator(validator);
        this.separator = "--";
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public void setValidator(DictionaryValidator validator) {
        this.validator = validator;
    }

    public DictionaryType getDictionaryType() {

        return dictionary.getType();
    }

    public DictionaryValidator getValidator() {
        return this.validator;
    }

    @Override
    public void setDictionary(DictionaryShell dictionary) {
        this.dictionary = dictionary;
    }


    @Override
    public Map<String, String> getDictionary() {
        try {
            Map<String, String> dictionary = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(this.dictionary.getPath()));
            String nextLine;
            String word, definition;
            while ((nextLine = reader.readLine()) != null) {
                String[] arr = nextLine.split(separator);
                word = arr[0].trim();
                definition = arr[1].trim();
                if (dictionary.containsKey(word)) {
                    dictionary.put(word, dictionary.get(word) + ", " + definition);
                } else {
                    dictionary.put(word, definition);
                }
            }
            reader.close();
            return dictionary;
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteWord(String word) {
        try {
            Map<String, String> dictionary = getDictionary();
            PrintWriter pw;
            pw = new PrintWriter(new FileWriter(this.dictionary.getPath(), false));
            pw.print("");
            pw.close();
            dictionary.forEach((key, value) -> {
                if (!key.equals(word)) {
                    try {
                        addWord(key, value);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDefinition(String word) {
        Map<String, String> dictionary = getDictionary();
        return dictionary.get(word);
    }

    @Override
    public void addWord(String word, String definition) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(this.dictionary.getPath(), true));
            pw.println(word + " -- " + definition);
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String dictionaryToString() {
        Map<String, String> dictionary = getDictionary();
        StringBuilder result = new StringBuilder();
        dictionary.forEach((word, definition) -> {
                    result.append(word).append(" - ").append(definition).append("\n");
                }
        );
        return result.toString();
    }
}
