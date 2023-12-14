package org.command;

import org.model.Joke;
import org.dao.JokeService;

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
    public String execute(String args) {
        Joke joke = jokeService.getRandomJoke();
        return "Анекдот №" + joke.getId() +
                "\n" + joke.getText();
    }
}
