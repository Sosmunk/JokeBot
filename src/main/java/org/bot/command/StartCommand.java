package org.bot.command;

import org.bot.bot.Bot;

/**
 * Команда /start
 * Отправить приветствие
 */
public class StartCommand implements BotCommand {
    @Override
    public void execute(String args, Long chatId, Bot bot) {
        bot.sendMessage(chatId, "Привет, я бот - любитель анекдотов. Чтобы получить справку о работе со мной напишите /help.");
    }
}
