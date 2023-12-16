package org.bot.bot;

/**
 *  Интерфейс работы ботов
 */
public interface Bot {
    /**
     * Отправка сообщений бота
     *
     * @param chatId  id чата
     * @param message сообщение, которое нужно отправить
     */
    default void sendMessage(Long chatId, String message) {}
}
