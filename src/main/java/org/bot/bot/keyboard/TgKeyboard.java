package org.bot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатура для ТГ Бота
 */
public class TgKeyboard extends KeyboardFactory {
	/**
	 * Создание клавиатуры
	 *
	 * @return Клавиатура
	 */
	public ReplyKeyboardMarkup createKeyboard() {
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRows = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		for (String button : createButtons()) {
			row.add("\n" + button);
		}
		keyboardRows.add(row);
		keyboardMarkup.setKeyboard(keyboardRows);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(true);
		return keyboardMarkup;
	}
}
