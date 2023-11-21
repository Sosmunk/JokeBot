package org.bot.commands;

/**
 * Команда /start
 */

public class StartCommand implements BotCommand {
    /**
     * Получить приветствие (/start)
     * @param args аргументы команды
     * @return приветствие
     */
    @Override
    public String execute(String args) {
        return "Привет, я бот - любитель анекдотов. Чтобы получить справку о работе со мной напишите /help.";
    }
}
