package org.bot.bots;

import org.bot.commands.CommandProcessor;
import org.bot.dto.CommandData;
import org.bot.dto.ParserCommand;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс телеграм бота
 */
public class TelegramJokeBot extends TelegramLongPollingBot implements JokeBot {
    private final CommandProcessor commandProcessor;
    private final ParserCommand parserCommand;
    public TelegramJokeBot() {
        commandProcessor = new CommandProcessor();
        parserCommand = new ParserCommand();
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
        try {
            // TODO: Функционал работы с анекдотами
            if (update.hasMessage() && update.getMessage().hasText()){
                Message textInMessage = update.getMessage();
                String chatId = textInMessage.getChatId().toString();
                SendMessage sendMessage = new SendMessage();

                // TODO: Функционал обработки команд
                CommandData request = parserCommand.parseMessage(textInMessage.getText());
                String result = commandProcessor.runCommand(request);
                System.out.println(result);
                //

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

    @Override
    public CommandData parseMessage(String text) {
        // TODO
        return null;
    }
}
