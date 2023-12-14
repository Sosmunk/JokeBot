package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;
import org.bot.enumerable.ChatPlatform;

/**
 * Команда /getJoke &lt;id&gt;
 * Получить анекдот по id
 */
public class GetJokeCommand implements BotCommand {

    private final JokeService jokeService;

    public GetJokeCommand(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        if (args == null) {
            return "Введите \"/getJoke <номер анекдота>\"";
        }
        int jokeId;

        try {
            jokeId = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return """
                    Неправильный номер команды! Ответ должен содержать только цифры.
                    Например: "/getJoke 1"
                    """;
        }

        Joke joke = this.jokeService.getJoke(jokeId);

        if (joke == null) {
            return "Анекдот не найден";
        }

        jokeService.saveLastJoke(chatId, joke.getId(), chatPlatform);

        String ratingString = joke.getAverageRatingString();

        return String.format("Анекдот №%s%n", joke.getId()) + joke.getText() + ratingString;
    }
}
