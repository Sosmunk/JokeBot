package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;

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
    public String execute(String args, Long chatId) {
        Joke joke = jokeService.getRandomJoke();
        return String.format("Анекдот №%s%n", joke.getId()) + joke.getText();
    }
}
