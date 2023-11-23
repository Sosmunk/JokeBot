package org.bot;

import org.bot.dao.JokeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeService implements JokeService {
    List<Joke> jokes = new ArrayList<>();

    @Override
    public Joke getJoke(Integer id) {
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

    @Override
    public Joke rateJoke(Integer id, Integer assesment) {
        // TODO : реализовать тест для оценивания шутки
        return null;
    }
}
