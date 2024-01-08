package org.bot.bot;

/**
 * Бот, отправляющий сообщения пользователям
 */
public interface Bot {
    /**
     * Отправка сообщений бота
     *
     * @param chatId  id чата
     * @param message сообщение, которое нужно отправить
     */
    void sendMessage(Long chatId, String message);

    /**
     * Отправить сообщение с клавиатурой
     *
     * @param chatId  id чата
     * @param message сообщение
     */
    void sendMessageWithRateKeyboard(Long chatId, String message);

    /**
     * Вернуть чат платформу бота
     *
     * @return чат платформа бота
     */
    String getChatPlatform();
}
