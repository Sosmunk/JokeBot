package org.bot.bots;

import org.bot.commands.CommandProcessor;
import org.bot.dto.CommandData;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс телеграм бота
 */
public class TelegramJokeBot extends TelegramLongPollingBot implements JokeBot<String> {
    private final CommandProcessor commandProcessor;

    public TelegramJokeBot(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message textInMessage = update.getMessage();
            String chatId = textInMessage.getChatId().toString();
            // TODO: Будет дублироваться, поправить
            CommandData commandData = commandProcessor.parseCommand(textInMessage.getText());
            String result = commandProcessor.runCommand(commandData);
            // ---

            sendMessage(chatId, result);
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

    @Override
    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        // TODO : Проверить работоспособность
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}