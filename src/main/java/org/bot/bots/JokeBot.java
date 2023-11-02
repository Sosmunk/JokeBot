package org.bot.bots;

import org.bot.dto.CommandData;

/**
 * Интерфейс, в котором должны
 */
public interface JokeBot {
    /**
     * Выделяет из сообщения команду и её аргументы
     * @param text текст сообщения
     * @return {@link CommandData}
     */
    CommandData parseMessage(String text);
}
