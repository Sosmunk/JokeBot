package org.bot.service;

import org.bot.Joke;
import org.bot.dao.JokeDAO;
import org.bot.utils.JokeDataSource;

import java.util.List;

/**
 * Сервис, отвечающий за работу с данными об анекдотах
 */
public class JokeServiceImpl implements JokeService {
    /**
     * JokeDAO добавлен для разделения бизнес-логики и логики работы с Hibernate
     * В будущем понадобится усложнить бизнес-логику (работа с чатами)
     */
    private final JokeDAO jokeDAO;

    public JokeServiceImpl(JokeDAO jokeDAO) {
        this.jokeDAO = jokeDAO;

        // Заполняем БД если анекдоты отсутствуют
        if (getRandomJoke() == null) {
            JokeDataSource jokeDataSource = new JokeDataSource();
            List<Joke> jokes = jokeDataSource.getJokeList();
            jokes.forEach(this::saveJoke);
        }

    }

    @Override
    public Joke getJoke(Integer id) {
        return jokeDAO.findJoke(id);
    }

    @Override
    public Joke getRandomJoke() {
        return jokeDAO.findJokeByRandom();
    }

    @Override
    public void saveJoke(Joke joke) {
        jokeDAO.save(joke);
    }
}
