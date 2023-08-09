package main;

import main.entities.Definition;
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
            sessionFactory = cfg.buildSessionFactory();
        } catch (Exception e) {
            System.out.println(e);
        }
        return sessionFactory;
    }
}
