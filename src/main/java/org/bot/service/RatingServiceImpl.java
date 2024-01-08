package org.bot.service;

import org.bot.Joke;
import org.bot.Rate;
import org.bot.dao.RatingDAO;

import java.util.List;
import java.util.Optional;

/**
 * Сервис отвечающий за работу с данными об оценках анекдотов
 */
public class RatingServiceImpl implements RatingService {
    private final RatingDAO ratingDAO;
    private final JokeService jokeService;

    public RatingServiceImpl(RatingDAO ratingDAO, JokeService jokeService) {
        this.ratingDAO = ratingDAO;
        this.jokeService = jokeService;
    }

    @Override
    public boolean rateJoke(Integer jokeId, Long chatId, Byte stars) {
        Joke joke = jokeService.getJoke(jokeId);
        if (joke == null) {
            return false;
        }
        Rate rate = ratingDAO.findRating(jokeId, chatId);

        if (rate != null) {
            ratingDAO.updateRating(rate, stars);
        } else {
            Rate newRate = new Rate(chatId, stars, joke);
            ratingDAO.saveRating(newRate);
        }
        return true;
    }

    /**
     * Получить средний рейтинг анекдота
     *
     * @param jokeId id анекдота
     * @return средний рейтинг анекдота, либо пустой optional
     */
    @Override
    public Optional<Double> getAverageRatingForJoke(Integer jokeId) {
        return ratingDAO.findAverageStarsForJoke(jokeId);
    }

    /**
     * Получить лучшие анекдоты по рейтингу
     * @return список id анекдотов
     */
    @Override
    public List<Integer> getBestJokeIds() {
        return ratingDAO.findBestJokeIdsByRating();
    }
}
