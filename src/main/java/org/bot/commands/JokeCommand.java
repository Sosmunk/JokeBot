package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;
import org.bot.enumerable.ChatPlatform;


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


        jokeService.saveLastJoke(chatId, joke.getId(), chatPlatform);

        String ratingString = joke.getAverageRatingString();

        return String.format("Анекдот №%s%n", joke.getId()) + joke.getText() + ratingString;
    }
}
