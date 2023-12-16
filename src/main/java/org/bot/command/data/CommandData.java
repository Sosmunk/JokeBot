package org.bot.command.data;

/**
 * Хранит в себе данные о командах, поступающие от пользователя
 *
 * @param command Команда (например /getJoke)
 * @param args    Аргументы (например: id="12")
 */
public record CommandData(String command, String args) {

}
