package org.bot.dao;

import org.bot.Rate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * DAO для работы с рейтингами анекдотов
 */
public class RatingDAO {

    private final SessionFactory sessionFactory;

    public RatingDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Найти рейтинг анекдота у определенного пользователя
     *
     * @param jokeId id анекдота
     * @param chatId id чата
     * @return рейтинг анекдота
     */
    public Rate findRating(Integer jokeId, Long chatId) {
        Session session = sessionFactory.openSession();
        Query<Rate> query = session.
                createQuery("from Rate where chatId =: chatId and joke.id =: jokeId", Rate.class)
                .setParameter("chatId", chatId)
                .setParameter("jokeId", jokeId);

        Rate rate = query.uniqueResult();
        session.close();
        return rate;
    }

    /**
     * Обновить рейтинг анекдота
     *
     * @param rate  рейтинг
     * @param stars оценка
     */
    public void updateRating(Rate rate, byte stars) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            rate.setStars(stars);
            session.merge(rate);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Сохранить рейтинг анекдота
     *
     * @param rate рейтинг
     */
    public void saveRating(Rate rate) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(rate);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }

}
