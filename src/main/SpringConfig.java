package main;


import main.entities.Dictionary;
import main.entities.DictionaryValidator;
import main.enums.DictionaryType;
import main.model.DictionaryShell;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("main")
@PropertySource("classpath:app.properties")
public class SpringConfig {
    @Value("${dictionaryAccess}")
    private String managerType;

    public SpringConfig() {
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    Pattern latinPattern = Pattern.compile("[a-zA-Z]{4}");
    Pattern numbersPattern = Pattern.compile("\\d{5}");


    @Value("${firstPath}")
    String firstPath;

    @Value("${secondPath}")
    String secondPath;

    public Map<DictionaryType, String> paths = new HashMap<>();
    public Map<DictionaryType, Pattern> validatorPatterns = new HashMap<>();

    @Bean
    public Map<DictionaryType, String> paths() {
        paths.put(DictionaryType.LATIN, firstPath);
        paths.put(DictionaryType.NUMBERS, secondPath);
        return this.paths;
    }

    @Bean
    public ArrayList<DictionaryShell> availableDictionaries() {
        ArrayList<DictionaryShell> result = new ArrayList<>();
        switch (managerType) {
            case "file" -> {
                getAvailableDictionaryFiles();
            }
            case "DB" -> {
                List<Dictionary> dictionaries = getAvailableDictionaryDB();
                dictionaries.forEach(dictionary -> {
                    DictionaryShell shell = new DictionaryShell();
                    shell.setId(dictionary.getId());
                    shell.setType(DictionaryType.valueOf(dictionary.getType()));
                    shell.setName(dictionary.getName());
                    result.add(shell);
                });
            }
        }

        return result;
    }

    private List<Dictionary> getAvailableDictionaryDB() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("FROM Dictionary ");
        List<Dictionary> dictionaries = query.getResultList();
        session.close();
        return dictionaries;
    }

    private void getAvailableDictionaryFiles() {

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
