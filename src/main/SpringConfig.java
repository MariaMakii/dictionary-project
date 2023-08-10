package main;


import main.entities.Dictionary;
import main.entities.DictionaryValidator;
import main.enums.DictionaryType;
import main.model.DictionaryShell;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.persistence.Query;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("main")
@PropertySource("classpath:app.properties")
public class SpringConfig{
    @Value("${spring.profiles.active}")
    private String managerType;

    @Value("${dictionaryFolderPath}")
    private String mainFolderPath;

    @Autowired
    public Environment env;

    @Bean
    public String getEnv(){
        return Arrays.toString(env.getActiveProfiles());
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    Pattern latinPattern = Pattern.compile("[a-zA-Z]{4}");
    Pattern numbersPattern = Pattern.compile("\\d{5}");


    public Map<DictionaryType, Pattern> validatorPatterns = new HashMap<>();

    @Bean
    public Map<DictionaryType, String> paths() {
        Map<DictionaryType, String> paths = new HashMap<>();
        File mainFolder = new File(mainFolderPath);
        File[] subfolders = mainFolder.listFiles(File::isDirectory);
        int dictionaryFileId = 0;
        assert subfolders != null;
        for (File folder : subfolders) {
            File[] dictionaryFiles = folder.listFiles(File::isFile);
            assert dictionaryFiles != null;
            for (File dictionaryFile : dictionaryFiles) {
                DictionaryShell dictionary = new DictionaryShell();
                dictionary.setName(dictionaryFile.getName());
                dictionary.setPath(dictionaryFile.getPath());
                dictionary.setId(dictionaryFileId);
                dictionary.setType(DictionaryType.valueOf(folder.getName()));
                dictionaryFileId++;
            }
        }
        return paths;
    }

    @Bean
    public ArrayList<DictionaryShell> availableDictionaries() {
        ArrayList<DictionaryShell> result = new ArrayList<>();
        switch (managerType) {
            case "file" -> {
                return getAvailableDictionaryFiles();
            }
            case "DB" -> {
                return getAvailableDictionaryDB();
            }
        }

        return result;
    }

    private DictionaryShell convertDictionariesToShells(Dictionary dictionary) {
        DictionaryShell shell = new DictionaryShell();
        shell.setType(DictionaryType.valueOf(dictionary.getType()));
        shell.setId(dictionary.getId());
        shell.setName(dictionary.getName());
        return shell;
    }

    private ArrayList<DictionaryShell> getAvailableDictionaryDB() {
        ArrayList<DictionaryShell> result = new ArrayList<>();
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Dictionary ");
        List<Dictionary> dictionaries = query.getResultList();
        dictionaries.forEach(dictionary -> {
            result.add(convertDictionariesToShells(dictionary));
        });
        session.close();
        return result;
    }

    private ArrayList<DictionaryShell> getAvailableDictionaryFiles() {
        ArrayList<DictionaryShell> result = new ArrayList<>();
        File mainFolder = new File(mainFolderPath);
        File[] subfolders = mainFolder.listFiles(File::isDirectory);
        int dictionaryFileId = 0;
        assert subfolders != null;
        for (File folder : subfolders) {
            File[] dictionaryFiles = folder.listFiles(File::isFile);
            assert dictionaryFiles != null;
            for (File dictionaryFile : dictionaryFiles) {
                DictionaryShell dictionary = new DictionaryShell();
                dictionary.setName(dictionaryFile.getName());
                dictionary.setPath(dictionaryFile.getPath());
                dictionary.setId(dictionaryFileId);
                dictionary.setType(DictionaryType.valueOf(folder.getName()));
                result.add(dictionary);
                dictionaryFileId++;
            }
        }
        return result;
    }

    private List<DictionaryValidator> getValidatorsDB() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM DictionaryValidator ");
        List<DictionaryValidator> validators = (List<DictionaryValidator>) query.getResultList().stream().collect(Collectors.toList());
        session.close();
        return validators;
    }

    private List<main.entities.DictionaryType> getDictionaryTypesDB() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM DictionaryType ");
        List<main.entities.DictionaryType> types = (List<main.entities.DictionaryType>) query.getResultList().stream().collect(Collectors.toList());
        session.close();
        return types;
    }

    private void setValidatorPatternsDB() {
        List<main.entities.DictionaryType> types = getDictionaryTypesDB();
        List<DictionaryValidator> validators = getValidatorsDB();
        types.forEach(type -> {
            List<DictionaryValidator> validatorList = validators.stream().filter(val ->
                    val.getId().equals(type.getValidator())
            ).collect(Collectors.toList());
            Pattern pattern = Pattern.compile(validatorList.get(0).getRegex());
            validatorPatterns.put(DictionaryType.valueOf(type.getType()), pattern);
        });
    }

    @Bean
    public Map<DictionaryType, Pattern> validatorPatterns() {
        switch (managerType) {
            case "file" -> {
                validatorPatterns.put(DictionaryType.LATIN, latinPattern);
                validatorPatterns.put(DictionaryType.NUMBERS, numbersPattern);
            }
            case "DB" -> {
                setValidatorPatternsDB();
            }
            default -> System.out.println("Не удалось загрузить регулярные выражения для валидотора");
        }
        return this.validatorPatterns;
    }
}
