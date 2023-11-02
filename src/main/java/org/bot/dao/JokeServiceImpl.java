package org.bot.dao;

import org.bot.Joke;

/**
 * Singleton класс, отвечающий за взаимодействие с базой данных joke
 */
public class JokeServiceImpl implements JokeService {

    private static volatile JokeService instance;
    private JokeServiceImpl() {}

    /**
     * Возвращает singleton instance класса
     * @return Joke Service
     */
    public static JokeService getInstance() {
        if (instance == null) {
            synchronized (JokeServiceImpl.class) {
                if (instance == null) {
                    instance = new JokeServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Joke getJoke(Integer id) {
        // TODO
        return null;
    }

    @Override
    public Joke getRandomJoke() {
        // TODO
        return null;
    }
}
