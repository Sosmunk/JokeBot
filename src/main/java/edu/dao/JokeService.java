package edu.dao;

import edu.model.Joke;

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
     */
    void saveJoke(Joke joke);
}
