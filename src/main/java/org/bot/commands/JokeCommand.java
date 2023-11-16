package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;

/**
 * Команда /joke
 */

public class JokeCommand implements BotCommand {
    private final JokeService jokeService;
    public JokeCommand(JokeService jokeService) {
        this.jokeService = jokeService;
    }
    /**
     * Получить случайную шутку (/joke)
     * @param args аргументы команды
     * @return случайная шутка
     */
    @Override
    public String execute(String args) {
        // TODO: работа с сервисом
        Joke joke = jokeService.getRandomJoke();
        return null;
    }
}
