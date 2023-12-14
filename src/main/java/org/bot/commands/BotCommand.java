package org.bot.commands;

import org.bot.enumerable.ChatPlatform;

/**
 * Команда бота
 */
public interface BotCommand {
    /**
     * Выполнить команду
     *
     * @param args         аргументы команды
     * @param chatId       id чата
     * @param chatPlatform чат платформа (Telegram/VK)
     * @return строку с результатом выполнения команды
     */
    String execute(String args, Long chatId, ChatPlatform chatPlatform);
}
