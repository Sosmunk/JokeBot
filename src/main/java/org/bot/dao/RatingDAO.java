package org.bot.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bot.Rate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

/**
 * DAO для работы с рейтингами анекдотов
 */
public class RatingDAO {

    private final SessionFactory sessionFactory;
    private final Logger logger = LogManager.getLogger();

    private final TransactionRunner transactionRunner;

    public RatingDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.transactionRunner = new TransactionRunner(sessionFactory);

    }

    /**
     * Найти рейтинг анекдота у определенного пользователя
     *
     * @param jokeId id анекдота
     * @param chatId id чата
     * @return рейтинг анекдота или null
     */
    public Rate findRating(Integer jokeId, Long chatId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Rate> query = session.
                    createQuery("from Rate where chatId =: chatId and joke.id =: jokeId", Rate.class)
                    .setParameter("chatId", chatId)
                    .setParameter("jokeId", jokeId);

            return query.uniqueResult();
        }
    }

    /**
     * Обновить рейтинг анекдота
     *
     * @param rate  рейтинг
     * @param stars оценка
     */
    public void updateRating(Rate rate, byte stars) {
        transactionRunner.doInTransaction((session -> {
            rate.setStars(stars);
            session.merge(rate);
        }));
    }

    /**
     * Сохранить рейтинг анекдота
     *
     * @param rate рейтинг
     */
    public void saveRating(Rate rate) {
        transactionRunner.doInTransaction((session -> session.persist(rate)));
    }

    /**
     * Найти все звезды рейтинга для конкретного анекдота
     *
     * @param jokeId id анекдота
     * @return звезды рейтинга
     */
    public List<Byte> findStarsForJoke(Integer jokeId) {
        try (Session session = sessionFactory.openSession()) {

            Query<Byte> query = session
                    .createQuery("select stars from Rate where joke.id = :jokeId", Byte.class)
                    .setParameter("jokeId", jokeId);

            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return List.of();
    }

}
