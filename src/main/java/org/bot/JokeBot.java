package org.bot;

/**
 *  Интерфейс работы ботов
 */
public interface JokeBot {
    /**
     * Отправка сообщений бота
     *
     * @param chatId  id чата
     * @param message сообщение, которое нужно отправить
     */
    default void sendMessage(Long chatId, String message) {}
}
