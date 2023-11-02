package org.bot.commands;

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

    public CommandProcessor() {
        commandMap = new HashMap<>();
        init();
    }

    private void init() {
        commandMap.put("/start", new StartCommand());
        commandMap.put("/help", new HelpCommand());
        commandMap.put("/joke", new JokeCommand());
    }

    /**
     * Выполнить пользовательскую команду
     * @param commandData данные о команде из сообщения
     * @return сообщение пользователю
     */
    public String runCommand(CommandData commandData) {
        try {
            System.out.println(commandData);
            BotCommand botCommand = commandMap.get(commandData.getCommand());
            return botCommand.execute(commandData.getArgs());
        } catch (NullPointerException e) {
            return "Ой-ой, такой команды не существует";
        }
    }
}
