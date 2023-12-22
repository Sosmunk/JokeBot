package org.bot.command;

import org.bot.Joke;
import org.bot.bot.Bot;
import org.bot.service.JokeService;
import org.bot.service.RatingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * /best отправить анекдоты с самым высоким рейтингом (до 10 шт)
 */
public class BestCommand implements BotCommand {
    private final RatingService ratingService;

    private final JokeService jokeService;

    public BestCommand(JokeService jokeService, RatingService ratingService) {
        this.jokeService = jokeService;
        this.ratingService = ratingService;
    }

    @Override
    public void execute(String args, Long chatId, Bot bot) {
        List<Integer> jokeIds = ratingService.getBestJokeIds();

        List<String> jokeStrings = new ArrayList<>();

        for (int jokeId : jokeIds) {

            Joke joke = jokeService.getJoke(jokeId);

            Optional<Double> averageRating = ratingService.getAverageRatingForJoke(jokeId);

            String ratingString = averageRating
                    .map(optionalDouble -> "\nРейтинг анекдота: " + optionalDouble)
                    .orElse("");

            jokeStrings.add("Анекдот №" + jokeId +
                    "\n" + joke.getText() + ratingString);
        }

        bot.sendMessage(chatId,
                "Лучшие анекдоты по мнению пользователей: \n\n" +
                        String.join("\n\n", jokeStrings));
    }
}
