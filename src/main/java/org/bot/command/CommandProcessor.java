package org.bot.command;

import org.bot.command.data.CommandData;
import org.bot.service.JokeService;
import org.bot.service.RatingService;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для обработки команд из сообщений бота
 */
public class CommandProcessor {

    /**
     * Хранилище ключ-значение для ассоциаций:
     * текст-команды : команда на выполнение
     */
    private final Map<String, BotCommand> commandMap;
    private final CommandParser commandParser;

    public CommandProcessor(JokeService jokeService, RatingService ratingService) {
        this.commandParser = new CommandParser();
        commandMap = new HashMap<>();
        GetJokeCommand getJokeCommand = new GetJokeCommand(jokeService, ratingService);
        RateLastCommand rateLastCommand = new RateLastCommand(jokeService, ratingService);
        commandMap.put("/start", new StartCommand());
        commandMap.put("/help", new HelpCommand());
        commandMap.put("/joke", new JokeCommand(jokeService, ratingService));
        commandMap.put("/getJoke", getJokeCommand);
        commandMap.put("/best", new BestCommand(getJokeCommand, ratingService));
        commandMap.put("/rate", new RateCommand(ratingService));
        commandMap.put("1☆", rateLastCommand);
        commandMap.put("2☆", rateLastCommand);
        commandMap.put("3☆", rateLastCommand);
        commandMap.put("4☆", rateLastCommand);
        commandMap.put("5☆", rateLastCommand);
    }

    /**
     * Выполнить пользовательскую команду
     *
     * @param command команда из сообщения
     * @param chatId  id чата
     * @return сообщение пользователю
     */
    public String runCommand(String command, Long chatId) {
        CommandData commandData = commandParser.parseMessage(command);
        BotCommand botCommand = commandMap.get(commandData.command());

        if (botCommand == null) {
            return "Команда не найдена";
        }
        if (botCommand.getClass() == RateLastCommand.class) {
            return botCommand.execute(commandData.command().substring(0, 1), chatId);
        } else {
            return botCommand.execute(commandData.args(), chatId);
        }
    }
}
