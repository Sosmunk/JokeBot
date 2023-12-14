package org.bot.commands;

import org.bot.dao.RatingService;
import org.bot.enumerable.ChatPlatform;

/**
 * Команда /rate #id_шутки #оценка <br>
 * Оценить анекдот
 */
public class RateCommand implements BotCommand {

    private final RatingService ratingService;

    public RateCommand(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        String[] data = args.split(" ");
        if (data.length != 2) {
            return "Неверное количество аргументов";
        }

        Integer jokeId = Integer.parseInt(data[0]);
        byte stars = Byte.parseByte(data[1]);

        if (stars > 5 || stars < 1) {
            return "Неверное количество звезд рейтинга";
        }

        return this.ratingService.rateJoke(jokeId, chatId, stars);
    }
}
