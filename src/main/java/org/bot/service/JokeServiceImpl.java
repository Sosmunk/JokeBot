package org.bot.service;

import org.bot.Joke;
import org.bot.dao.JokeDAO;
import org.bot.service.data.JokeDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис, отвечающий за работу с данными об анекдотах
 */
public class JokeServiceImpl implements JokeService {
    /**
     * JokeDAO добавлен для разделения бизнес-логики и логики работы с Hibernate
     */
    private final JokeDAO jokeDAO;

    /**
     * Хранилище последних анекдотов отправленных в телеграм чат <br>
     * ключ - id чата <br>
     * значение - id анекдота
     */
    private final Map<Long, Integer> chatLastJokes;
    
    public JokeServiceImpl(JokeDAO jokeDAO) {
        this.jokeDAO = jokeDAO;

        // Заполняем БД если анекдоты отсутствуют
        if (jokeDAO.isEmpty()) {
            JokeDataSource jokeDataSource = new JokeDataSource();
            List<Joke> jokes = jokeDataSource.getJokeList();
            jokes.forEach(this::saveJoke);
        }

        this.chatLastJokes = new HashMap<>();

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

    @Override
    public Integer getLastJokeId(Long chatId) {
        return chatLastJokes.get(chatId);
    }

    @Override
    public void saveLastJoke(Long chatId, Integer jokeId) {
        chatLastJokes.put(chatId, jokeId);
    }
}
