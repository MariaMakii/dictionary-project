package main.entities;

import main.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DefinitionDAO implements Dao<Definition> {
    @Override
    public Definition get(long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Definition definition = session.get(Definition.class, id);
        transaction.commit();
        session.close();
        return definition;
    }

    @Override
    public List<Definition> getAll() {
        List<Definition> definitions = (List<Definition>) HibernateSessionFactory.getSessionFactory().openSession()
                .createQuery("From Definition").list();
        return definitions;
    }

    public List<Definition> getAll(int dictionary) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createQuery("From Definition where dictionary = :dictionaryType");
        query.setParameter("dictionaryType", dictionary);
        List<Definition> definitions = query.list();
        return definitions;
    }

    @Override
    public void save(Definition definition) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(definition);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Definition definition) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(definition);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Definition definition) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(definition);
        transaction.commit();
        session.close();
    }
}