package org.bot;

import org.bot.bots.TelegramJokeBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс, включающий ботов
 */

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.registerBotTg();
    }

    /**
     * Подключение бота тг
     */
    void registerBotTg(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramJokeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}