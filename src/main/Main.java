package main;

import main.view.DictionaryApp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        DictionaryApp app1 = (DictionaryApp) ctx.getBean("dictionaryApp");
        app1.run();
    }
}
