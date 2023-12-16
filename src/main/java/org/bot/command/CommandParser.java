package org.bot.command;

import org.bot.command.data.CommandData;

/**
 * Парсер команд
 */
public class CommandParser {
    /**
     * Возвращает отформатированное сообщение в виде CommandData
     *
     * @param message сообщение пользователя
     * @return CommandData сообщение
     */
    public CommandData parseMessage(String message){
        if (message == null || message.isEmpty() || message.isBlank()) {
            return new CommandData(null, null);
        }
        String[] messageParams = message.split(" ",2);
        return new CommandData(messageParams[0],(messageParams.length > 1)
                ? messageParams[1]
                : null);
    }
}
