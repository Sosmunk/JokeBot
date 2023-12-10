package org.bot.dao;
import org.bot.Joke;
import org.bot.utils.DataLoader;

/**
 * Сервис, отвечающий за работу с данными об анекдотах
 */
public class JokeServiceImpl implements JokeService {
    private final JokeDAO jokeDAO;

    public JokeServiceImpl(JokeDAO jokeDAO) {
        this.jokeDAO = jokeDAO;
        DataLoader dataLoader = new DataLoader();
        dataLoader.populate(this);
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
}
