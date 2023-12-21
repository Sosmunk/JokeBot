package org.bot.bot;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.messages.Send;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.basic.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bot.bot.keyboard.KeyboardFactory;
import org.bot.command.CommandProcessor;

/**
 * VK бот
 */
public class VkBot extends LongPollBot implements Bot {

    private final CommandProcessor commandProcessor;
    private final Logger logger = LogManager.getLogger();

    private final String vkToken;

    public VkBot(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        this.vkToken = System.getenv("VK_TOKEN");
    }
  
    private final Keyboard vkRateKeyBoard = new KeyboardFactory().createVKRateKeyboard();
  
    /**
     * Метод, отвечающий за обработку сообщений, присланных пользователем
     *
     * @param messageNew сообщение от пользователя
     */
    @Override
    public void onMessageNew(MessageNew messageNew) {
        Message message = messageNew.getMessage();
        if (message.hasText()) {
            String result = commandProcessor.runCommand(
                    message.getText(),
                    message.getPeerId().longValue());
            sendMessage(message.getPeerId().longValue(), result);
        }
    }

    /**
     * Получение токена доступа к VK API
     *
     * @return токен VK api
     */
    @Override
    public String getAccessToken() {
        return vkToken;
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        try {
            Send send = vk.messages.send()
                    .setPeerId(chatId.intValue())
                    .setMessage(message);
            if (message.contains("Анекдот №")) {
                send.setKeyboard(vkRateKeyBoard);
            }
            send.execute();
        } catch (VkApiException e) {
            logger.error("Не удалось отправить сообщение!", e);
        }

    }

    /**
     * Запуск бота
     */
    public void start() {
        try {
            this.startPolling();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось запустить бота!");
        }
    }
}
