package main;

import main.view.DictionaryApp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
        DictionaryApp app1 = (DictionaryApp) ctx.getBean("app");
        app1.run();
    }
}
