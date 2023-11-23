package org.bot.commands;

import org.bot.Joke;
import org.bot.dao.JokeService;

/**
 * Команда /rate <id> <stars 1-5>
 */
public class RateCommand implements BotCommand{

    private final JokeService jokeService;

    public RateCommand(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    /**
     * Оценить шутку по <id>
     * @param args аргументы команды
     * @return оценка шутки
     */
    @Override
    public String execute(String args) {
        // TODO : доработать оценку шутки
        if (args == null || args.isEmpty()){
            return "Введите \"/rate <номер анекдота> <оценка анекдота от 1 до 5>\"";
        }
        String[] argsArray = args.split(" ");
        int jokeId, assessment;

        try {
            jokeId = Integer.parseInt(argsArray[0]);
            assessment = Integer.parseInt(argsArray[1]);
        } catch (NumberFormatException e) {
            return """
                    Неправильный номер команды! Ответ должен содержать только цифры.
                    Например: "/rate 1 1"
                    """;
        }

        Joke joke = this.jokeService.rateJoke(jokeId, assessment);
        if (joke == null) {
            return "Анекдот не найден";
        }

        return String.format("Анекдот №%s", joke.getId()) + " оценён!";
    }
}
