package org.bot.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.bot.Joke;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Тесты для JokeDAO
 */
public class JokeDAOTest {

    private final SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
    private final JokeDAO jokeDAO = new JokeDAO(sessionFactory);

    private final Session session = Mockito.mock(Session.class);

    private final String FIRST_JOKE = """
            — Заходит программист в лифт, а ему надо на 12—й этаж.
            — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
            """;
    private final Joke joke = new Joke(FIRST_JOKE);

    /**
     * Тест правильного сохранения шутки в БД
     */
    @Test
    public void testSave() {
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(Mockito.mock(Transaction.class));

        Joke savedJoke = jokeDAO.save(joke);

        Mockito.verify(sessionFactory,
                Mockito.times(1)).openSession();
        Mockito.verify(session,
                Mockito.times(1)).beginTransaction();
        Mockito.verify(session,
                Mockito.times(1)).persist(joke);
        Mockito.verify(session,
                Mockito.times(1)).close();

        Assert.assertEquals(joke, savedJoke);
    }

    /**
     * Тест поиска анекдота в БД
     */
    @Test
    public void testFindJoke() {
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.get(Joke.class, 1)).thenReturn(joke);

        Joke foundJoke = jokeDAO.findJoke(1);

        Mockito.verify(sessionFactory,
                Mockito.times(1)).openSession();
        Mockito.verify(session,
                Mockito.times(1)).get(Joke.class, 1);
        Mockito.verify(session,
                Mockito.times(1)).close();

        Assert.assertEquals(joke, foundJoke);
    }
}