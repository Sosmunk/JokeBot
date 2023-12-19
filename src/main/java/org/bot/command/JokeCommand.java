package org.bot.command;

import org.bot.Joke;
import org.bot.enumerable.ChatPlatform;
import org.bot.service.JokeService;

/**
 * Команда /joke
 * Получить случайный анекдот
 */

public class JokeCommand implements BotCommand {
    private final JokeService jokeService;

    public JokeCommand(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        Joke joke = jokeService.getRandomJoke();
        if (joke == null) {
            return "Анекдоты не найдены";
        }

        jokeService.saveLastJoke(chatId, joke.getId(), chatPlatform);

        String averageRating = joke.getAverageRatingString();

        if (!averageRating.equals("")) {
            averageRating = "\n" + averageRating;
        }

        return "Анекдот №" + joke.getId() +
                "\n" + joke.getText() + averageRating;
    }
}
