package main;

import main.entities.Definition;
import main.entities.Dictionary;
import main.entities.DictionaryType;
import main.entities.DictionaryValidator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactory() {
    }

    public static SessionFactory getSessionFactory() {
        try {
            Configuration cfg = new Configuration().configure();
            cfg.addAnnotatedClass(Definition.class);
            cfg.addAnnotatedClass(DictionaryType.class);
            cfg.addAnnotatedClass(DictionaryValidator.class);
            cfg.addAnnotatedClass(Dictionary.class);
            sessionFactory = cfg.buildSessionFactory();
        } catch (Exception e) {
            System.out.println(e);
        }
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
