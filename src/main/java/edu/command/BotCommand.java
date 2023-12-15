package edu.command;

/**
 * Команда бота
 */
public interface BotCommand {
    /**
     * Выполнить команду
     * @param args аргументы команды
     * @return строку с результатом выполнения команды
     */
    String execute(String args);
}
