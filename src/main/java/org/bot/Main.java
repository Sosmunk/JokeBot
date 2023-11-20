package org.bot;

import org.bot.bots.BotLogic;
import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
import org.bot.commands.CommandProcessor;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;
import org.bot.utils.DataLoader;

/**
 * Класс, включающий ботов
 */

public class Main {
    public static void main(String[] args) {
        DataLoader dataLoader = new DataLoader();
        JokeService jokeService = new JokeServiceImpl();
        dataLoader.populate(jokeService);


        CommandProcessor commandProcessor = new CommandProcessor(jokeService);
        TelegramJokeBot telegramJokeBot = new TelegramJokeBot(commandProcessor);
        VkJokeBot vkJokeBot = new VkJokeBot(commandProcessor);
        BotLogic botLogic = new BotLogic(telegramJokeBot, vkJokeBot);
        telegramJokeBot.start();
        vkJokeBot.start();
    }

}