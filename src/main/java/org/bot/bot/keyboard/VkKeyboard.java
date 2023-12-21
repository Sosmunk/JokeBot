package org.bot.bot.keyboard;

import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.TextButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатура для ВК Бота
 */
public class VkKeyboard extends KeyboardFactory {
	/**
	 * Создание клавиатуры
	 *
	 * @return клавиатура
	 */
	public Keyboard createKeyboard() {
		List<List<Button>> buttons = new ArrayList<>();
		List<Button> buttonRow = new ArrayList<>();
		List<String> listTextButton = createButtons();
		for (String textButton : listTextButton) {
			buttonRow.add(new TextButton(Button.Color.POSITIVE, new TextButton.Action(textButton)));
		}
		buttons.add(buttonRow);
		Keyboard keyboard = new Keyboard(buttons);
		keyboard.setOneTime(true);
		return keyboard;
	}
}
