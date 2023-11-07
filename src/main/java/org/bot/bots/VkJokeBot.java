package org.bot.bots;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;

/**
 * VK Бот для работы с анекдотами
 */
public class VkJokeBot extends LongPollBot implements JokeBot {

    /**
     * Метод, отвечающий за обработку сообщений, присланных пользователем
     * @param messageNew сообщение от пользователя
     */
    @Override
    public void onMessageNew(MessageNew messageNew) {
        try {
            // TODO: Функционал работы с анекдотами
            Message message = messageNew.getMessage();
            if (message.hasText()) {
                String response = "Ваше сообщение " + message.getText();
                vk.messages.send()
                        .setPeerId(message.getPeerId())
                        .setMessage(response)
                        .execute();
            }
        } catch (VkApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение токена доступа к VK API
     * @return токен VK api
     */
    @Override
    public String getAccessToken() {
        return System.getenv("VK_TOKEN");
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
