package org.bot.command;

import org.bot.dao.RatingService;
import org.bot.enumerable.ChatPlatform;

/**
 * Команда /rate &lt;id шутки&gt; &lt;оценка&gt; <br>
 * Оценить анекдот
 */
public class RateCommand implements BotCommand {

    private final RatingService ratingService;

    public RateCommand(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    private static final String INVALID_ARGUMENT_COUNT = "Неверное количество аргументов";

    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        if (args == null) {
            return INVALID_ARGUMENT_COUNT;
        }
        String[] data = args.split(" ");
        if (data.length != 2) {
            return INVALID_ARGUMENT_COUNT;
        }

        Integer jokeId = Integer.parseInt(data[0]);
        byte stars;
        try {
            stars = Byte.parseByte(data[1]);
        } catch (NumberFormatException e) {
            return "Количество звезд рейтинга должно содержать только цифры (1-5)";
        }


        if (stars > 5 || stars < 1) {
            return "Неверное количество звезд рейтинга";
        }

        return this.ratingService.rateJoke(jokeId, chatId, stars);
    }
}
