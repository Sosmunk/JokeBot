package org.bot.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandData that = (CommandData) o;
        return Objects.equals(command, that.command) && Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, args);
    }
}
