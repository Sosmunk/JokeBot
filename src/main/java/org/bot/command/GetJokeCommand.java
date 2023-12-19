package org.bot.command;

import org.bot.Joke;

import org.bot.enumerable.ChatPlatform;

import org.bot.service.JokeService;

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

        String averageRating = joke.getAverageRatingString();

        if (!averageRating.isEmpty()) {
            averageRating = "\n" + averageRating;
        }
        return "Анекдот №" + joke.getId() +
                "\n" + joke.getText() + averageRating;
    }
}
