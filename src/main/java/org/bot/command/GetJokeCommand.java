package org.bot.command;

import org.bot.Joke;
import org.bot.bot.Bot;
import org.bot.service.JokeService;
import org.bot.service.RatingService;

import java.util.Optional;

/**
 * Команда /getJoke &lt;id&gt;
 * Получить анекдот по id и отправить его в бота
 */
public class GetJokeCommand implements BotCommand {

    private final JokeService jokeService;
    private final RatingService ratingService;

    public GetJokeCommand(JokeService jokeService, RatingService ratingService) {
        this.jokeService = jokeService;
        this.ratingService = ratingService;
    }

    @Override
    public void execute(String args, Long chatId, Bot bot) {
        if (args == null) {
            bot.sendMessage(chatId, "Введите \"/getJoke <номер анекдота>\"");
            return;
        }
        int jokeId;

        try {
            jokeId = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            bot.sendMessage(chatId,
                    """
                            Неправильный номер команды! Ответ должен содержать только цифры.
                            Например: "/getJoke 1"
                            """);
            return;
        }

        Joke joke = this.jokeService.getJoke(jokeId);

        if (joke == null) {
            bot.sendMessage(chatId, "Анекдот не найден");
            return;
        }

        jokeService.saveLastJoke(chatId, joke.getId());

        Optional<Double> averageRating = ratingService.getAverageRatingForJoke(joke.getId());

        String ratingString = averageRating.isPresent()
                ? "\nРейтинг анекдота: " + averageRating.get()
                : "";

        bot.sendMessageWithRateKeyboard(chatId, "Анекдот №" + joke.getId() +
                "\n" + joke.getText() + ratingString);
    }
}
