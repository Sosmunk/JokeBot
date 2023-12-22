package org.bot.bot;

/**
 * Данные о сообщении
 *
 * @param text     текст сообщения
 * @param chatId   id чата
 * @param keyboard клавиатура
 */
public record MessageData(String text, Long chatId, boolean keyboard) {

}
