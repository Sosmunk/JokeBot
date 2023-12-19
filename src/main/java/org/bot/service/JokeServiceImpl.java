package org.bot.service;

import org.bot.Joke;
import org.bot.dao.JokeDAO;
import org.bot.enumerable.ChatPlatform;
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
    private final Map<Long, Integer> telegramChatLastJokes;

    /**
     * Хранилище последних анекдотов отправленных в ВК чат <br>
     * ключ - id чата <br>
     * значение - id анекдота
     */
    private final Map<Long, Integer> vkChatLastJokes;

    public JokeServiceImpl(JokeDAO jokeDAO) {
        this.jokeDAO = jokeDAO;

        // Заполняем БД если анекдоты отсутствуют
        if (jokeDAO.isEmpty()) {
            JokeDataSource jokeDataSource = new JokeDataSource();
            List<Joke> jokes = jokeDataSource.getJokeList();
            jokes.forEach(this::saveJoke);
        }
        this.telegramChatLastJokes = new HashMap<>();
        this.vkChatLastJokes = new HashMap<>();

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
    public Integer getLastJokeId(Long chatId, ChatPlatform chatPlatform) {
        if (chatPlatform == ChatPlatform.TELEGRAM) {
            return telegramChatLastJokes.getOrDefault(chatId, null);
        } else if (chatPlatform == ChatPlatform.VK) {
            return vkChatLastJokes.getOrDefault(chatId, null);
        }
        return null;
    }

    @Override
    public void saveLastJoke(Long chatId, Integer jokeId, ChatPlatform chatPlatform) {
        if (chatPlatform == ChatPlatform.TELEGRAM) {
            telegramChatLastJokes.put(chatId, jokeId);
        } else if (chatPlatform == ChatPlatform.VK) {
            vkChatLastJokes.put(chatId, jokeId);
        }
    }
}
