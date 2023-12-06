package org.bot.commands;

import org.bot.dao.JokeService;
import org.bot.dto.CommandData;
import org.bot.dto.CommandParser;

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

    public CommandProcessor(JokeService jokeService) {
        this.commandParser = new CommandParser();
        commandMap = new HashMap<>();
        commandMap.put("/start", new StartCommand());
        commandMap.put("/help", new HelpCommand());
        commandMap.put("/joke", new JokeCommand(jokeService));
        commandMap.put("/getJoke", new GetJokeCommand(jokeService));
    }

    /**
     * Выполнить пользовательскую команду
     * @param commandData данные о команде из сообщения
     * @return сообщение пользователю
     */
    public String runCommand(CommandData commandData) {
        BotCommand botCommand = commandMap.get(commandData.command());
        if (botCommand == null) {
            return "Команда не найдена";
        }
        return botCommand.execute(commandData.args());
    }

    public CommandData parseCommand(String command) {
        return commandParser.parseMessage(command);
    }
}
