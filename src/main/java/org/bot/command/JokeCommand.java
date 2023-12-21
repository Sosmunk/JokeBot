package org.bot.command;

import org.bot.Joke;
import org.bot.service.JokeService;
import org.bot.service.RatingService;

import java.util.OptionalDouble;

/**
 * Команда /joke
 * Получить случайный анекдот
 */
public class JokeCommand implements BotCommand {
    private final JokeService jokeService;
    private final RatingService ratingService;

    public JokeCommand(JokeService jokeService, RatingService ratingService) {
        this.jokeService = jokeService;
        this.ratingService = ratingService;
    }

    @Override
    public String execute(String args, Long chatId) {
        Joke joke = jokeService.getRandomJoke();
        if (joke == null) {
            return "Анекдоты не найдены";
        }

        jokeService.saveLastJoke(chatId, joke.getId());

        OptionalDouble averageRating = ratingService.getAverageRatingForJoke(joke.getId());

        String ratingString = averageRating.isPresent()
                ? "\nРейтинг анекдота: " + averageRating.getAsDouble()
                : "";

        return "Анекдот №" + joke.getId() +
                "\n" + joke.getText() + ratingString;

    }
}
