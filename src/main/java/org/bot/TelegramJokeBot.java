package org.bot;

import org.command.CommandProcessor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс телеграм бота
 */
public class TelegramJokeBot extends TelegramLongPollingBot implements JokeBot {
    private final CommandProcessor commandProcessor;
    private final Logger logger = LogManager.getLogger();

    /**
     * Имя бота
     */
    private final String BOT_NAME = System.getenv("TG_BOT_NAME");

    public TelegramJokeBot(BotConfiguration configuration, CommandProcessor commandProcessor) {
        super(configuration.getTgToken());
        this.commandProcessor = commandProcessor;
        start();
    }

    /**
     *Метод, который получает сообщение от пользователя и отправляет ему новое в ответ
     * @param update обновление из api
     */
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message textInMessage = update.getMessage();
            Long chatId = textInMessage.getChatId();
            String result = commandProcessor.runCommand(textInMessage.getText());
            sendMessage(chatId, result);
        }
    }

    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Не удалось отправить сообщение!", e);
        }
    }

    /**
     * Запуск бота
     */
    private void start(){
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error("Не удалось запустить бота!", e);
        }
    }
}