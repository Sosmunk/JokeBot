package org.bot.command;

import org.bot.Joke;
import org.bot.service.JokeService;
import org.bot.service.RatingService;

import java.util.Optional;

/**
 * Команда /getJoke &lt;id&gt;
 * Получить анекдот по id
 */
public class GetJokeCommand implements BotCommand {

    private final JokeService jokeService;
    private final RatingService ratingService;

    public GetJokeCommand(JokeService jokeService, RatingService ratingService) {
        this.jokeService = jokeService;
        this.ratingService = ratingService;
    }

    @Override
    public String execute(String args, Long chatId) {
        if (args == null) {
            return "Введите \"/getJoke <номер анекдота>\"";
        }
        int jokeId;

        try {
            jokeId = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return """
                    Неправильный номер команды! Ответ должен содержать только цифры.
                    Например: "/getJoke 1"
                    """;
        }

        Joke joke = this.jokeService.getJoke(jokeId);

        if (joke == null) {
            return "Анекдот не найден";
        }

        jokeService.saveLastJoke(chatId, joke.getId());

        Optional<Double> averageRating = ratingService.getAverageRatingForJoke(joke.getId());

        String ratingString = averageRating.isPresent()
                ? "\nРейтинг анекдота: " + averageRating.get()
                : "";

        return "Анекдот №" + joke.getId() +
                "\n" + joke.getText() + ratingString;
    }
}
