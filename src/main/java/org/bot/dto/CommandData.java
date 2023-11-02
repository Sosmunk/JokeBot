package org.bot.dto;

/**
 * Хранит в себе данные о командах, поступающие от пользователя
 */
public class CommandData {
    /**
     * Команда (например /getJoke)
     */
    String command;

    /**
     * Аргументы (например: id="12")
     */
    String args;
}
