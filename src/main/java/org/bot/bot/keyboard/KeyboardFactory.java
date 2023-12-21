package org.bot.bot.keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатура
 */
public class KeyboardFactory {
	/**
	 * Определение содержимого клавиатуры
	 *
	 * @return list всех кнопок клавиатуры
	 */
	protected List<String> createButtons() {
		List<String> buttonRow = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			buttonRow.add(i + "☆");
		}
		return buttonRow;
	}
}
