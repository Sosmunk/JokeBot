package org.bot.commands;

/**
 * Команда /start
 * Получить приветствие
 */

public class StartCommand implements BotCommand {
    @Override
    public String execute(String args, Long chatId) {
        return "Привет, я бот - любитель анекдотов. Чтобы получить справку о работе со мной напишите /help.";
    }
}
