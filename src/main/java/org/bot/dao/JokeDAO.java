package org.bot.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bot.Joke;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * DAO для выполнения CRUD операций с БД анекдотов
 */

public class JokeDAO {
    private final SessionFactory sessionFactory;
    private final Random random = new Random();
    private final Logger logger = LoggerFactory.getLogger(JokeDAO.class);
    private final TransactionRunner transactionRunner;

    public JokeDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.transactionRunner = new TransactionRunner(sessionFactory);
    }

    /**
     * Получить количество строчек в таблице анекдотов
     *
     * @param session сессия
     * @return количество строчек в таблице анекдотов
     */
    public Long getRowCount(Session session) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

        Root<Joke> countRoot = countQuery.from(Joke.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        return session.createQuery(countQuery).uniqueResult();
    }

    /**
     * Проверка на то что таблица анекдотов пустая
     *
     * @return true если таблица анекдотов пустая, иначе false
     */
    public boolean isEmpty() {
        try (Session session = sessionFactory.openSession()) {
            return getRowCount(session) == 0;
        }
    }


    /**
     * Поиск анекдота в БД
     *
     * @param id id анекдота
     * @return анекдот
     */
    public Joke findJoke(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Joke.class, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Поиск слуайного анекдота в БД
     *
     * @return анекдот
     */
    public Joke findJokeByRandom() {
        try (Session session = sessionFactory.openSession()) {
            List<Joke> jokes;
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            Long rowCount = getRowCount(session);

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
            jokes = query.setFirstResult(randomInt).setMaxResults(1).getResultList();

            return jokes.get(0);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * Сохранение анекдота в БД
     *
     * @param joke анекдот
     */
    public void save(Joke joke) {
        transactionRunner.doInTransaction(session -> session.persist(joke));
    }
}
