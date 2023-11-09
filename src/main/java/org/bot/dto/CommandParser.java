package org.bot.dto;

/**
 * Класс парсера команд, вводимых пользователем
 */
public class CommandParser {
    /**
     * Возвращает отформатированное сообщение в виде CommandData
     * @param message сообщение пользователя
     * @return CommandData сообщение
     */
    public CommandData parseMessage(String message){
        String[] messageParams = message.split(" ",2);
        CommandData commandData = new CommandData(messageParams[0],(messageParams.length > 1) ? messageParams[1] : null);
        return commandData;
    }
}
