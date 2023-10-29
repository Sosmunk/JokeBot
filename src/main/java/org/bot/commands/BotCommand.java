package org.bot.commands;

/**
 * Интерфейс, отвечающий за реализацию команд
 */
public interface BotCommand {
    /**
     * Выполнить команду
     * @param args аргументы команды
     * @return строку с результатом выполнения команды
     */
    String executeCommand(String args);
}
