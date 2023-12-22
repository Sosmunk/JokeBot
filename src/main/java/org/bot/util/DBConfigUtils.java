package org.bot.util;

import org.bot.Joke;
import org.bot.Rate;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилитарный класс для получения объектов из конфигураций баз данных
 */
public class DBConfigUtils {

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

    /**
     * Получить Postgres DataSource из connection.properties
     *
     * @return Postgres DataSource
     */
    public PGSimpleDataSource getPGDataSource() {
        try (InputStream input = new FileInputStream("src/main/resources/connection.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
            pgSimpleDataSource.setDatabaseName(properties.getProperty("db.name"));
            pgSimpleDataSource.setPortNumber(Integer.parseInt(properties.getProperty("db.port")));
            pgSimpleDataSource.setUser(properties.getProperty("db.user"));
            pgSimpleDataSource.setPassword(properties.getProperty("db.password"));

            return pgSimpleDataSource;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не найтден файл конфигурации");
        } catch (IOException e) {
            // TODO: exception
            throw new RuntimeException(e);
        }
    }
}
