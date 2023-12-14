package org.bot;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.basic.Message;
import org.command.CommandProcessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс VK бота
 */
public class VkJokeBot extends LongPollBot implements JokeBot {

    private final CommandProcessor commandProcessor;
    private final Logger logger = LogManager.getLogger();

    private final String vkToken;

    public VkJokeBot(BotConfiguration configuration, CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        this.vkToken = configuration.getVkToken();
        start();
    }

    /**
     * Метод, отвечающий за обработку сообщений, присланных пользователем
     *
     * @param messageNew сообщение от пользователя
     */

    public void onMessageNew(MessageNew messageNew) {
        Message message = messageNew.getMessage();
        if (message.hasText()) {
            String result = commandProcessor.runCommand(message.getText());
            sendMessage(Long.valueOf(message.getPeerId()), result);
        }
    }

    /**
     * Получение токена доступа к VK API
     *
     * @return токен VK api
     */
    public String getAccessToken() {
        return vkToken;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        try {
            vk.messages.send()
                    .setPeerId((chatId.intValue()))
                    .setMessage(message)
                    // TODO : Здесь реализовать клавиатуру для VK бота
                    .execute();
        } catch (VkApiException e) {
            logger.error("Не удалось отправить сообщение!", e);
        }
    }

    /**
     * Запуск бота
     */
    private void start(){
        try {
            this.startPolling();
        } catch (VkApiException e) {
            logger.error("Не удалось запустить бота!", e);
        }
    }
}
