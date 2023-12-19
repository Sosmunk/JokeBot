package org.bot.dao;

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
     * @return результат операции
     */
    String rateJoke(Integer jokeId, Long chatId, Byte stars);
}
