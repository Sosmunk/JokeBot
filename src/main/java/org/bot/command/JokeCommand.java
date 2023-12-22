package org.bot.command;

import org.bot.Joke;
import org.bot.bot.Bot;
import org.bot.service.JokeService;
import org.bot.service.RatingService;

import java.util.Optional;

/**
 * Команда /joke
 * Отправить случайный анекдот
 */
public class JokeCommand implements BotCommand {
    private final JokeService jokeService;
    private final RatingService ratingService;

    public JokeCommand(JokeService jokeService, RatingService ratingService) {
        this.jokeService = jokeService;
        this.ratingService = ratingService;
    }

    @Override
    public void execute(String args, Long chatId, Bot bot) {
        Joke joke = jokeService.getRandomJoke();
        if (joke == null) {
            bot.sendMessage(chatId, "Анекдоты не найдены");
            return;
        }

        jokeService.saveLastJoke(chatId, joke.getId());

        Optional<Double> averageRating = ratingService.getAverageRatingForJoke(joke.getId());

        String ratingString = averageRating.isPresent()
                ? "\nРейтинг анекдота: " + averageRating.get()
                : "";

        bot.sendMessage(chatId, "Анекдот №" + joke.getId() +
                "\n" + joke.getText() + ratingString);

    }
}
