package org.bot.dao;

import org.bot.Joke;
import org.bot.enumerable.ChatPlatform;
import org.bot.utils.DataLoader;

import java.util.HashMap;
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
        DataLoader dataLoader = new DataLoader();
        dataLoader.populate(this);
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
    public Joke saveJoke(Joke joke) {
        return jokeDAO.save(joke);
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
