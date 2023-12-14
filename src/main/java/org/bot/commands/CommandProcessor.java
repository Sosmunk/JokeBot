package org.bot.commands;

import org.bot.dao.JokeService;
import org.bot.dao.RatingService;
import org.bot.dto.CommandData;
import org.bot.dto.CommandParser;
import org.bot.enumerable.ChatPlatform;

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
        RateLastCommand rateLastCommand = new RateLastCommand(jokeService, ratingService);
        commandMap.put("/start", new StartCommand());
        commandMap.put("/help", new HelpCommand());
        commandMap.put("/joke", new JokeCommand(jokeService));
        commandMap.put("/getJoke", new GetJokeCommand(jokeService));
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
     * @param command      команда из сообщения
     * @param chatId       id чата
     * @param chatPlatform чат платформа (Telegram/VK)
     * @return сообщение пользователю
     */
    public String runCommand(String command, Long chatId, ChatPlatform chatPlatform) {
        CommandData commandData = parseCommand(command);
        BotCommand botCommand = commandMap.get(commandData.command());

        if (botCommand == null) {
            return "Команда не найдена";
        }
        if (botCommand.getClass() == RateLastCommand.class) {
            return botCommand.execute(commandData.command().substring(0, 1), chatId, chatPlatform);
        } else {
            return botCommand.execute(commandData.args(), chatId, chatPlatform);
        }

    }

    private CommandData parseCommand(String command) {
        return commandParser.parseMessage(command);
    }
}
