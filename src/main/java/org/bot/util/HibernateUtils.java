package org.bot.util;

import org.bot.Joke;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Утилитарный класс для Hibernate
 */
public class HibernateUtils {

    /**
     * Создать SessionFactory
     *
     * @return SessionFactory
     */
    public SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setProperty(
                "hibernate.connection.username",
                System.getenv("DB_NAME"));
        configuration.setProperty(
                "hibernate.connection.password",
                System.getenv("DB_PASSWORD"));
        configuration.addAnnotatedClass(Joke.class);

        return configuration.buildSessionFactory();
    }
}
