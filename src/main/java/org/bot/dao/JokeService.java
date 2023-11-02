package org.bot.dao;

import org.bot.Joke;

public interface JokeService {
    /**
     * Получить анекдот по id
     * @param id id анекдота
     * @return анекдот
     */
    Joke getJoke(Integer id);

    /**
     * Получить случайный анекдот
     * @return анекдот
     */
    Joke getRandomJoke();
}
