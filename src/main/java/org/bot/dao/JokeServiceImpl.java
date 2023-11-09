package org.bot.dao;

import org.bot.Joke;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Singleton класс, отвечающий за взаимодействие с базой данных joke
 */
public class JokeServiceImpl implements JokeService {

    private final SessionFactory sessionFactory;

    public JokeServiceImpl() {
        Configuration configuration = new Configuration();
        configuration.setProperty(
                "hibernate.connection.url",
                "jdbc:postgresql://localhost:5433/" + System.getenv("DB"));
        configuration.setProperty(
                "hibernate.connection.username",
                System.getenv("DB_NAME"));
        configuration.setProperty(
                "hibernate.connection.password",
                System.getenv("DB_PASSWORD"));
        configuration.addAnnotatedClass(Joke.class);
        this.sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Joke getJoke(Integer id) {
        Session session = sessionFactory.openSession();
        Joke joke = session.get(Joke.class, id);
        session.close();
        return joke;
    }

    @Override
    public Joke getRandomJoke() {
        // TODO
        return null;
    }

    @Override
    public Joke saveJoke(Joke joke) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(joke);
        transaction.commit();
        session.close();
        return joke;
    }
}
