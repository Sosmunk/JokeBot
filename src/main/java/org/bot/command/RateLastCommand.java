package org.bot.command;

import org.bot.dao.RatingService;
import org.bot.enumerable.ChatPlatform;
import org.bot.service.JokeService;

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
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        Integer jokeId = jokeService.getLastJokeId(chatId, chatPlatform);
        if (jokeId == null) {
            return "Нет анекдотов для оценивания";
        }

        return ratingService.rateJoke(jokeId, chatId, Byte.parseByte(args));
    }
}
