package org.bot.command;

import org.bot.bot.Bot;
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
    public void execute(String args, Long chatId, Bot bot) {
        Integer jokeId = jokeService.getLastJokeId(chatId);
        if (jokeId == null) {
            bot.sendMessage(chatId, "Нет анекдотов для оценивания");
            return;
        }

        ratingService.rateJoke(jokeId, chatId, Byte.parseByte(args));
        bot.sendMessage(chatId, "Анекдот оценен");
    }
}
