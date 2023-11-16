package org.bot.bots;

/**
 * Интерфейс работы ботов
 */
public interface JokeBot<T> {
    void sendMessage(T chatId, String message);

    // Если этот метод здесь не нужен, то как нам запускать бота?
    void start();
}
