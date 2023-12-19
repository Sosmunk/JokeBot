package org.bot.dao;

import org.bot.Joke;
import org.bot.Rate;
import org.bot.service.JokeService;

public class RatingServiceImpl implements RatingService {

    private final RatingDAO ratingDAO;

    private final JokeService jokeService;

    public RatingServiceImpl(RatingDAO ratingDAO, JokeService jokeService) {
        this.ratingDAO = ratingDAO;
        this.jokeService = jokeService;
    }

    /**
     * Оценить анекдот
     *
     * @param jokeId id анекдота
     * @param chatId id чата
     * @param stars  количество звезд (1-5)
     * @return результат оценки анекдота
     */
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
