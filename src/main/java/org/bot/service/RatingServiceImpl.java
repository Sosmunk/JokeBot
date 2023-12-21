package org.bot.service;

import org.bot.Joke;
import org.bot.Rate;
import org.bot.dao.RatingDAO;

import java.util.List;
import java.util.OptionalDouble;

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

    @Override
    public OptionalDouble getAverageRatingForJoke(Integer jokeId) {
        List<Byte> ratings = ratingDAO.findStarsForJoke(jokeId);

        return ratings.stream()
                .mapToDouble(a -> a)
                .average();
    }
}
