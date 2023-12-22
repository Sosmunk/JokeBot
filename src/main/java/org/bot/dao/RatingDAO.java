package org.bot.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bot.Rate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

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
     * Найти среднее количество звезд рейтинга для анекдота
     *
     * @param jokeId id анекдота
     * @return среднее количество звезд рейтинга, либо пустой Optional
     */
    public Optional<Double> findAverageStarsForJoke(Integer jokeId) {
        try (Session session = sessionFactory.openSession()) {

            Query<Double> query = session
                    .createQuery("select avg(stars) from Rate where joke.id = :jokeId", Double.class)
                    .setParameter("jokeId", jokeId);

            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Найти id лучших анекдотов по рейтингу (убывание)
     * @return список id анекдотов
     */
    public List<Integer> findBestJokeIdsByRating() {
        try (Session session = sessionFactory.openSession()) {

           return session
                    .createQuery(
                            "select joke.id from Rate group by joke.id order by avg(stars) desc",
                            Integer.class)
                   .setMaxResults(10)
                   .getResultList();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return List.of();
    }

}
