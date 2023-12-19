package org.bot.command;

import org.bot.enumerable.ChatPlatform;

/**
 * Команда /start
 * Получить приветствие
 */

public class StartCommand implements BotCommand {
    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        return "Привет, я бот - любитель анекдотов. Чтобы получить справку о работе со мной напишите /help.";
    }
}
