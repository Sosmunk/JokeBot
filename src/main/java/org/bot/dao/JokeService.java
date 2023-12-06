package org.bot.dao;

import org.bot.Joke;

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
}
