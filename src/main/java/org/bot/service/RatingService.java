package org.bot.service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для оценок анекдотов
 */
public interface RatingService {
    /**
     * Оценить анекдот
     *
     * @param jokeId id анекдота
     * @param chatId id чата
     * @param stars  количество звезд (1-5)
     * @return false, если анекдот не найден, иначе true
     */
    boolean rateJoke(Integer jokeId, Long chatId, Byte stars);

    /**
     * Получить средний рейтинг анекдота
     *
     * @param jokeId id анекдота
     * @return средний рейтинг, если анекдот был оценен
     */
    Optional<Double> getAverageRatingForJoke(Integer jokeId);

    /**
     * Получить лучшие анекдоты по рейтингу
     * @return список id анекдотов
     */
    List<Integer> getBestJokeIds();
}
