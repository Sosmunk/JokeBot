package org.bot.command;

import org.bot.bot.Bot;

/**
 * Команда бота
 */
public interface BotCommand {
    /**
     * Выполнить команду и отправить результат в бота
     *
     * @param args   аргументы команды
     * @param chatId id чата
     */
    void execute(String args, Long chatId, Bot bot);
}
