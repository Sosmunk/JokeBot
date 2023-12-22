package org.bot.bot.keyboard;

import java.util.Arrays;
import java.util.List;

/**
 * Все кнопки для клавиатуры
 */
public class KeyboardUtils {
	private static final List<String> LIST_RATES = Arrays.asList("1☆", "2☆", "3☆", "4☆", "5☆");

	/**
	 * Получить список звезд для клавиатуры
	 *
	 * @return список звезд для клавиатуры
	 */
	public List<String> getListRates() {
		return LIST_RATES;
	}

}
