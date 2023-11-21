package org.bot.bots;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;
import org.bot.commands.CommandProcessor;
import org.bot.dto.CommandData;

/**
 * VK Бот для работы с анекдотами
 */
public class VkJokeBot extends LongPollBot implements JokeBot<Integer> {

    private final CommandProcessor commandProcessor;

    public VkJokeBot(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    /**
     * Метод, отвечающий за обработку сообщений, присланных пользователем
     *
     * @param messageNew сообщение от пользователя
     */
    @Override
    public void onMessageNew(MessageNew messageNew) {
        // TODO: Функционал работы с анекдотами
        Message message = messageNew.getMessage();
        if (message.hasText()) {
            // Дублирование кода
            CommandData commandData = commandProcessor.parseCommand(message.getText());
            String result = commandProcessor.runCommand(commandData);
            //
            sendMessage(message.getPeerId(), result);
        }
    }

    /**
     * Получение токена доступа к VK API
     *
     * @return токен VK api
     */
    @Override
    public String getAccessToken() {
        return System.getenv("VK_TOKEN");
    }

    @Override
    public void sendMessage(Integer chatId, String message) {
        try {
            vk.messages.send()
                    .setPeerId(chatId)
                    .setMessage(message)
                    .execute();
        } catch (VkApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start() {
        try {
            this.startPolling();
        } catch (VkApiException e) {
            e.printStackTrace();
        }
    }

}
