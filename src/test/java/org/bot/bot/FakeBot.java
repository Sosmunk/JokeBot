package org.bot.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Имитация чат бота
 */
public class FakeBot implements Bot {

    /**
     * Сообщения которые должны быть отправлены пользователю
     */
    private final List<MessageData> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(new MessageData(message, chatId, false));
    }

    @Override
    public void sendMessageWithRateKeyboard(Long chatId, String message) {
        messages.add(new MessageData(message, chatId, true));

    }

    /**
     * Получить текст последнего сообщения
     *
     * @return текст последнего сообщения
     */
    public String getLastMessageText() {
        return messages.get(messages.size() - 1).text();
    }

    /**
     * Получить chatId последнего сообщения
     *
     * @return chatId последнего сообщения
     */
    public Long getLastMessageChatId() {
        return messages.get(messages.size() - 1).chatId();
    }

    /**
     * Вернуть наличие клавиатуры в сообщении
     *
     * @return true, если клавиатура была отправлена
     */
    public Boolean getKeyboardPresent() {
        return messages.get(messages.size() - 1).keyboard();
    }
}
