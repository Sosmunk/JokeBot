package org.bot.dto;

/**
 * Хранит в себе данные о командах, поступающие от пользователя
 */
public class CommandData {
    public CommandData(String command, String args) {
        this.command = command;
        this.args = args;
    }

    /**
     * Команда (например /getJoke)
     */
    private final String command;

    /**
     * Аргументы (например: id="12")
     */
    private final String args;

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }
}
