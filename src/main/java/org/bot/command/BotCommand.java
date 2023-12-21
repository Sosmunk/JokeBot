package org.bot.command;

/**
 * Команда бота
 */
public interface BotCommand {
    /**
     * Выполнить команду
     *
     * @param args   аргументы команды
     * @param chatId id чата
     * @return строку с результатом выполнения команды
     */
    String execute(String args, Long chatId);
}
