package org.bot.dao;
import org.bot.Joke;

/**
 * Сервис, отвечающий за работу с данными об анекдотах
 */
public class JokeServiceImpl implements JokeService {
    private final JokeDAO jokeDAO;

    public JokeServiceImpl() {
        this.jokeDAO = new JokeDAO();
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
