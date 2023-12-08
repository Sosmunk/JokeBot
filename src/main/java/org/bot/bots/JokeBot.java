package org.bot.bots;

/**
 * Интерфейс работы ботов
 *  Параметризация необходима для chatId, поскольку VKBot и TGBot
 *  используют разные типы данных
 */
public interface JokeBot<T> {
    /**
     * Отправка сообщений бота
     * @param chatId id чата
     * @param message сообщение, которое нужно отправить
     */
    void sendMessage(T chatId, String message);

    // Если этот метод здесь не нужен, то как нам запускать бота?
    void start();
}
