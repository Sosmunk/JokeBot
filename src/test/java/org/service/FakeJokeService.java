package org.service;

import org.dao.JokeService;
import org.model.Joke;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Моковый сервис для тестирования функционала CommandProcessor
 */
public class FakeJokeService implements JokeService {
    /**
     * Структура данных, имитирующая БД
     */
    private final List<Joke> jokes = new ArrayList<>();

    @Override
    public Joke getJoke(Integer id) {
        if (id < 0 || id > jokes.size() + 1) {
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
    public void saveJoke(Joke joke) {
        joke.setId(jokes.size() + 1);
        jokes.add(joke);
    }
}
