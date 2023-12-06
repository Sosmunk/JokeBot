package org.bot.bots;

/**
 * Класс отвечающий за реализацию логики ботов
 */
public class BotLogic {
    private final TelegramJokeBot telegramJokeBot;
    private final VkJokeBot vkJokeBot;

    public BotLogic(TelegramJokeBot telegramJokeBot, VkJokeBot vkJokeBot) {
        this.telegramJokeBot = telegramJokeBot;
        this.vkJokeBot = vkJokeBot;
    }

    //TODO: Отправка сообщений в нужного бота по расписанию

}
