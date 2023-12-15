package edu.command;

import edu.model.Joke;
import edu.dao.JokeService;

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
