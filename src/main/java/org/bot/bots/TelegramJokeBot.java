package org.bot.bots;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс телеграм бота
 */
public class TelegramJokeBot extends TelegramLongPollingBot {
    /**
     * Токен телеграм бота
     */
    private final String TOKEN = System.getenv("TG_TOKEN");
    /**
     * Имя бота
     */
    private final String BOT_NAME = System.getenv("TG_BOT_NAME");

    /**
     *Метод, который получает сообщение от пользователя и отправляет ему новое в ответ
     * @param update обновление из api
     */
    @Override
    public void onUpdateReceived(Update update) {
        // TODO
        try {
            if (update.hasMessage() && update.getMessage().hasText()){
                Message textInMessage = update.getMessage();
                String chatId = textInMessage.getChatId().toString();
                SendMessage sendMessage = new SendMessage();

                String response = "Ваше сообщение: " + textInMessage.getText();

                sendMessage.setChatId(chatId);
                sendMessage.setText(response);

                execute(sendMessage);
            }
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
