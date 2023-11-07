package org.bot.bots;

/**
 * Класс для хранения логики бота
 */
public class BotLogic {

    private final JokeBot jokeBot;
    public BotLogic(JokeBot bot) {
        jokeBot = bot;
    }

    /**
     * Запускает бота
     */
    public void startBot() {
        jokeBot.start();
    }

}
