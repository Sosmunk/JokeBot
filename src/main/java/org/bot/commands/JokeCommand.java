package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;

/**
 * Команда /joke
 */

public class JokeCommand implements BotCommand {
    JokeService jokeService;
    public JokeCommand() {
        jokeService = JokeServiceImpl.getInstance();
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
