package org.bot.util;

import org.bot.Joke;
import org.bot.Rate;
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
        configuration.addAnnotatedClass(Joke.class);
        configuration.addAnnotatedClass(Rate.class);

        return configuration.buildSessionFactory();
    }
}
