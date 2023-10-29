package org.bot.commands;

/**
 * Интерфейс, отвечающий за реализацию команд
 */
public interface BotCommand {
    /**
     * Выполнить команду
     */
    String executeCommand(String args);
}
