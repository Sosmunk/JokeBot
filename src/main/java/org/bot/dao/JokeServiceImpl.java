package org.bot.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bot.Joke;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

/**
 * Singleton класс, отвечающий за взаимодействие с базой данных joke
 */
public class JokeServiceImpl implements JokeService {

    private final SessionFactory sessionFactory;
    private final Random random = new Random();

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
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        // Находим количество строчек в таблице
        Root<Joke> countRoot = countQuery.from(Joke.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        Long rowCount = session.createQuery(countQuery).uniqueResult();

        //Выборка по классу Joke
        CriteriaQuery<Joke> jokeQuery = criteriaBuilder.createQuery(Joke.class);
        Root<Joke> jokeRoot = jokeQuery.from(Joke.class);
        jokeQuery.select(jokeRoot);

        //Получаем список случайных шуток,
        //где будет храниться одна шутка (из коробки только так)
        int randomInt = random.nextInt(rowCount.intValue());
        Query<Joke> query = session.createQuery(jokeQuery);
        List<Joke> jokes = query.setFirstResult(randomInt).setMaxResults(1).getResultList();

        session.close();
        // БД может быть пустой, возможно нужно обработать этот случай
        return jokes.get(0);
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

    @Override
    public Joke rateJoke(Integer id, Integer assesment) {
        // TODO : добавить реализацию оценки шутки
        return null;
    }
}
