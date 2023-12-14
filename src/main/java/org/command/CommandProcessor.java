package org.command;

import org.dao.JokeService;
import org.util.CommandData;
import org.util.CommandParser;

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
     * @param command команда из сообщения
     * @return сообщение пользователю
     */
    public String runCommand(String command) {
        CommandData commandData = parseCommand(command);
        BotCommand botCommand = commandMap.get(commandData.command());
        if (botCommand == null){
            return "Команда не найдена";
        }
        return botCommand.execute(commandData.args());
    }

    private CommandData parseCommand(String command) {
        return commandParser.parseMessage(command);
    }
}
