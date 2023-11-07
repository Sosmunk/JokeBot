package org.bot;

import org.bot.bots.BotLogic;
import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;

/**
 * Класс, включающий ботов
 */

public class Main {
    public static void main(String[] args) {
        JokeService jokeService = new JokeServiceImpl();
        BotLogic telegramBotLogic = new BotLogic(new TelegramJokeBot(jokeService));
        BotLogic vkBotLogic = new BotLogic(new VkJokeBot(jokeService));
        telegramBotLogic.startBot();
        vkBotLogic.startBot();
    }

}