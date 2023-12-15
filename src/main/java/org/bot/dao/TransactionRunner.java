package org.bot.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Используется для правильного выполнения транзакций
 */
public class TransactionRunner {

    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(TransactionRunner.class);

    public TransactionRunner(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Выполнить операцию в транзакции
     *
     * @param call consumer в котором выполняются действия использующие сессию
     */
    public void doInTransaction(Consumer<Session> call) {
        try (Session session = sessionFactory.getCurrentSession()) {
            final Transaction transaction = session.beginTransaction();
            try {
                call.accept(session);
                transaction.commit();
            } catch (final Exception e) {
                transaction.rollback();
                logger.error(e.getMessage());
            }
        }
    }
}
