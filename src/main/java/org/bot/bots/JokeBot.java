package org.bot.bots;

/**
 * Интерфейс работы ботов <br>
 * <br>
 * Параметризация необходима для chatId, поскольку VKBot и TGBot <br>
 * используют разные типы данных для отправки сообщений
 */
public interface JokeBot<T> {
    /**
     * Отправка сообщений бота
     * @param chatId id чата
     * @param message сообщение, которое нужно отправить
     */
    void sendMessage(T chatId, String message);
    void start();
}
