package main;

import main.enums.DictionaryType;
import main.model.DictionaryFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
@ComponentScan("main")
@PropertySource("classpath:app.properties")
public class SpringConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    Pattern firstPattern = Pattern.compile("[a-zA-Z]{4}");
    Pattern secondPattern = Pattern.compile("\\d{5}");

    @Value("${firstPath}")
    String firstPath;

    @Value("${secondPath}")
    String secondPath;

    public Map<DictionaryType, String> paths = new HashMap<>();
    public Map<DictionaryType, Pattern> validatorPatterns = new HashMap<>();

    @Bean
    public Map<DictionaryType, String> paths() {
        paths.put(DictionaryType.FIRST, firstPath);
        paths.put(DictionaryType.SECOND, secondPath);
        return this.paths;
    }

    @Bean
    public Map<DictionaryType, Pattern> validatorPatterns() {
        validatorPatterns.put(DictionaryType.FIRST, firstPattern);
        validatorPatterns.put(DictionaryType.SECOND, secondPattern);
        return this.validatorPatterns;
    }

    @Bean
    public DictionaryFile dictionary1() {
        return new DictionaryFile(DictionaryType.FIRST, firstPath);
    }

    @Bean
    public DictionaryFile dictionary2() {
        return new DictionaryFile(DictionaryType.SECOND, secondPath);
    }
}
