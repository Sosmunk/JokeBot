package org.dao;
import org.model.Joke;
import org.util.DataLoader;

/**
 * Сервис, отвечающий за работу с данными об анекдотах
 */
public class JokeServiceImpl implements JokeService {
    /**
     * JokeDAO добавлен для разделения бизнес-логики и логики работы с Hibernate
     * В будущем понадобится усложнить бизнес-логику (работа с чатами)
     */
    private final JokeDAO jokeDAO;

    public JokeServiceImpl(JokeDAO jokeDAO) {
        this.jokeDAO = jokeDAO;
        DataLoader dataLoader = new DataLoader();
        dataLoader.addJokes(this);
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
    public void saveJoke(Joke joke) {
        jokeDAO.save(joke);
    }
}
