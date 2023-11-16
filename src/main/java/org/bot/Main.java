package org.bot;

import org.bot.bots.BotLogic;
import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
import org.bot.commands.CommandProcessor;
import org.bot.dao.JokeServiceImpl;

/**
 * Класс, включающий ботов
 */

public class Main {
    public static void main(String[] args) {
        CommandProcessor commandProcessor = new CommandProcessor(new JokeServiceImpl());
        TelegramJokeBot telegramJokeBot = new TelegramJokeBot(commandProcessor);
        VkJokeBot vkJokeBot = new VkJokeBot(commandProcessor);
        BotLogic botLogic = new BotLogic(telegramJokeBot, vkJokeBot);
    }

}