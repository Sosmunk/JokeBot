package org.bot.dao;

import org.bot.Joke;
import org.bot.enumerable.ChatPlatform;

/**
 * Сервис для получения анекдотов из хранилища
 */
public interface JokeService {
    /**
     * Получить анекдот по id
     *
     * @param id id анекдота
     * @return анекдот
     */
    Joke getJoke(Integer id);

    /**
     * Получить случайный анекдот
     *
     * @return анекдот
     */
    Joke getRandomJoke();

    /**
     * Сохранить анекдот
     *
     * @param joke анекдот
     * @return сохраненный анекдот
     */
    Joke saveJoke(Joke joke);

    /**
     * Получить id последнего анекдота в чате
     *
     * @param chatId       id чата
     * @param chatPlatform чат-платформа
     * @return
     */
    Integer getLastJokeId(Long chatId, ChatPlatform chatPlatform);

    /**
     * Сохранить последний анекдот отправленный пользователю в чат
     *
     * @param chatId       id чата
     * @param jokeId       id анекдота
     * @param chatPlatform чат-платформа
     */
    void saveLastJoke(Long chatId, Integer jokeId, ChatPlatform chatPlatform);

}
