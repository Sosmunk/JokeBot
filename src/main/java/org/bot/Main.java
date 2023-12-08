package org.bot;

import org.bot.bots.BotLogic;
import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
import org.bot.commands.CommandProcessor;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;

/**
 * Класс, для запуска программы
 */

public class Main {
    public static void main(String[] args) {
        JokeService jokeService = new JokeServiceImpl();
        CommandProcessor commandProcessor = new CommandProcessor(jokeService);
        TelegramJokeBot telegramJokeBot = new TelegramJokeBot(commandProcessor);
        VkJokeBot vkJokeBot = new VkJokeBot(commandProcessor);
        BotLogic botLogic = new BotLogic(telegramJokeBot, vkJokeBot);
        telegramJokeBot.start();
        vkJokeBot.start();
    }

}