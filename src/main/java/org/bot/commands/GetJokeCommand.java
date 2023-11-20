package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;

/**
 * /getJoke
 */
public class GetJokeCommand implements BotCommand {

    private final JokeService jokeService;

    public GetJokeCommand(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    /**
     * Получить анекдот по id
     *
     * @param args id анекдота
     * @return Анекдот
     */
    @Override
    public String execute(String args) {
        // TODO: Валидация аргументов
        Joke joke = this.jokeService.getJoke(Integer.parseInt(args));
        if (joke == null) {
            return "Анекдот не найден";
        }
        return joke.getText();
    }
}
