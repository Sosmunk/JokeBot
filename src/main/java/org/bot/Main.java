package org.bot;

import org.bot.bots.BotLogic;
import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
/**
 * Класс, включающий ботов
 */

public class Main {
    public static void main(String[] args) {
        BotLogic telegramBotLogic = new BotLogic(new TelegramJokeBot());
        BotLogic vkBotLogic = new BotLogic(new VkJokeBot());
        telegramBotLogic.startBot();
        vkBotLogic.startBot();
    }

}