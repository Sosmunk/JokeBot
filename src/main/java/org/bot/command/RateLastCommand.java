package org.bot.command;

import org.bot.service.JokeService;
import org.bot.service.RatingService;

/**
 * Оценить последний анекдот отправленный пользователю
 */
public class RateLastCommand implements BotCommand {

    private final JokeService jokeService;
    private final RatingService ratingService;

    public RateLastCommand(JokeService jokeService, RatingService ratingService) {
        this.jokeService = jokeService;
        this.ratingService = ratingService;
    }


    @Override
    public String execute(String args, Long chatId) {
        Integer jokeId = jokeService.getLastJokeId(chatId);
        if (jokeId == null) {
            return "Нет анекдотов для оценивания";
        }

        ratingService.rateJoke(jokeId, chatId, Byte.parseByte(args));
        return "Анекдот оценен";
    }
}
