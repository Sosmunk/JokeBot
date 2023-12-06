package org.bot;

import org.bot.dao.JokeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Моковый сервис для тестирования функционала CommandProcessor
 */
public class FakeService implements JokeService {
    /**
     * Структура данных, имитирующая БД
     */
    private final List<Joke> jokes = new ArrayList<>();

    @Override
    public Joke getJoke(Integer id) {
        if (id < 0 || id > jokes.size()) {
            return null;
        }
        return jokes.get(id - 1);
    }

    @Override
    public Joke getRandomJoke() {
        Random random = new Random();
        return jokes.get(random.nextInt(0, jokes.size()));
    }

    @Override
    public Joke saveJoke(Joke joke) {
        joke.setId(jokes.size() + 1);
        jokes.add(joke);
        return joke;
    }
}
