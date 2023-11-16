package org.bot.bots;

/**
 * Класс отвечающий за реализацию логики ботов
 */
public class BotLogic {

    // Верно ли то что мы здесь из BotLogic будем отправлять сообщения по расписанию?
    // Если задумка на лекции была такова,
    // тогда эти поля в будущем должны здесь присутствовать
    private final TelegramJokeBot telegramJokeBot;
    private final VkJokeBot vkJokeBot;

    public BotLogic(TelegramJokeBot telegramJokeBot, VkJokeBot vkJokeBot) {
        this.telegramJokeBot = telegramJokeBot;
        this.vkJokeBot = vkJokeBot;

        // Пока не знаю где правильно запускать ботов
        // Не хочется делать ботов публичными
        telegramJokeBot.start();
        vkJokeBot.start();

    }

    //TODO: Отправка сообщений в нужного бота по расписанию

}
