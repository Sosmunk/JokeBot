package org.bot.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bot.Joke;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Random;

/**
 * DAO для выполнения CRUD операций с БД анекдотов
 */
public class JokeDAO {
    private final SessionFactory sessionFactory;
    private final Random random = new Random();

    private final String ddlAuto;

    public String getDdlAuto() {
        return ddlAuto;
    }

    public JokeDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.ddlAuto = sessionFactory.getProperties().get("hibernate.hbm2ddl.auto").toString();
    }

    /**
     * Поиск анекдота в БД
     *
     * @param id id анекдота
     * @return анекдот
     */
    public Joke findJoke(Integer id) {
        Session session = sessionFactory.openSession();
        Joke joke = session.get(Joke.class, id);
        session.close();
        return joke;
    }

    /**
     * Поиск слуайного анекдота в БД
     *
     * @return анекдот
     */
    public Joke findJokeByRandom() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        // Находим количество строчек в таблице
        Root<Joke> countRoot = countQuery.from(Joke.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        Long rowCount = session.createQuery(countQuery).uniqueResult();

        if (rowCount == 0) {
            return null;
        }

        //Выборка по классу Joke
        CriteriaQuery<Joke> jokeQuery = criteriaBuilder.createQuery(Joke.class);
        Root<Joke> jokeRoot = jokeQuery.from(Joke.class);
        jokeQuery.select(jokeRoot);

        //Получаем список случайных анекдотов,
        //где будет храниться один анекдот (из коробки только так)
        int randomInt = random.nextInt(rowCount.intValue());
        Query<Joke> query = session.createQuery(jokeQuery);
        List<Joke> jokes = query.setFirstResult(randomInt).setMaxResults(1).getResultList();

        session.close();

        return jokes.get(0);
    }

    /**
     * Сохранение анекдота в БД
     *
     * @param joke анекдот
     * @return анекдот с id в базе данных
     */
    public Joke save(Joke joke) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(joke);
        transaction.commit();
        session.close();
        return joke;
    }
}
