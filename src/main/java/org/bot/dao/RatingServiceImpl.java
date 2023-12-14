package org.bot.dao;

import org.bot.Joke;
import org.bot.Rate;

public class RatingServiceImpl implements RatingService {

    private final RatingDAO ratingDAO;

    private final JokeService jokeService;

    public RatingServiceImpl(RatingDAO ratingDAO, JokeService jokeService) {
        this.ratingDAO = ratingDAO;
        this.jokeService = jokeService;
    }

    @Override
    public String rateJoke(Integer jokeId, Long chatId, Byte stars) {
        Joke joke = jokeService.getJoke(jokeId);
        if (joke == null) {
            return "Анекдот не найден";
        }
        Rate rate = ratingDAO.findRating(jokeId, chatId);

        if (rate != null) {
            ratingDAO.updateRating(rate, stars);
        } else {
            Rate newRate = new Rate(chatId, stars, joke);
            ratingDAO.saveRating(newRate);
        }

        return "Анекдот оценен";
    }
}
