package org.bot.commands;

import org.bot.dao.JokeService;
import org.bot.dto.CommandData;

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

    public CommandProcessor(JokeService jokeService) {
        commandMap = new HashMap<>();
        commandMap.put("/start", new StartCommand());
        commandMap.put("/help", new HelpCommand());
        commandMap.put("/joke", new JokeCommand(jokeService));
    }

    /**
     * Выполнить пользовательскую команду
     * @param commandData данные о команде из сообщения
     * @return сообщение пользователю
     */
    public String runCommand(CommandData commandData) {
        System.out.println(commandData);
        BotCommand botCommand = commandMap.get(commandData.getCommand());
        if (botCommand == null) {
            return "Команда не найдена";
        }
        return botCommand.execute(commandData.getArgs());
    }
}
