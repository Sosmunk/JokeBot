package org.bot.bots;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.messages.Send;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.basic.Message;
import org.bot.bots.keyboard.KeyboardFactory;
import org.bot.commands.CommandProcessor;
import org.bot.enumerable.ChatPlatform;

/**
 * Класс VK бота
 */
public class VkJokeBot extends LongPollBot implements JokeBot<Integer> {

    private final CommandProcessor commandProcessor;

    public VkJokeBot(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
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
                    message.getPeerId().longValue(),
                    ChatPlatform.VK);
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
            Send send = vk.messages.send()
                    .setPeerId(chatId)
                    .setMessage(message);
            if (message.contains("Анекдот №")) {
                send.setKeyboard(vkRateKeyBoard);
            }
            send.execute();
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
