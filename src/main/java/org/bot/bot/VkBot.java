package org.bot.bot;

import api.longpoll.bots.LongPollBot;
import api.longpoll.bots.exceptions.VkApiException;
import api.longpoll.bots.methods.impl.messages.Send;
import api.longpoll.bots.model.events.messages.MessageNew;
import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.TextButton;
import api.longpoll.bots.model.objects.basic.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bot.bot.keyboard.KeyboardUtils;
import org.bot.command.CommandProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * VK бот
 */
public class VkBot extends LongPollBot implements Bot {

	private final CommandProcessor commandProcessor;
	private final Logger logger = LogManager.getLogger();

	private final Keyboard rateKeyboard;

	private final List<String> listRate;

	private final String vkToken;

	public VkBot(CommandProcessor commandProcessor) {
		this.commandProcessor = commandProcessor;
		this.vkToken = System.getenv("VK_TOKEN");
		listRate = new KeyboardUtils().getListRates();
		this.rateKeyboard = createRateKeyboard();
	}

	/**
	 * Метод, отвечающий за обработку сообщений, присланных пользователем
	 *
	 * @param messageNew сообщение от пользователя
	 */
	@Override
	public void onMessageNew(MessageNew messageNew) {
		Message message = messageNew.getMessage();
		if (message.hasText()) {
			commandProcessor.runCommand(
					message.getText(),
					message.getPeerId()
							.longValue(),
					this);
		}
	}

	/**
	 * Получение токена доступа к VK API
	 *
	 * @return токен VK api
	 */
	@Override
	public String getAccessToken() {
		return vkToken;
	}

	@Override
	public void sendMessage(Long chatId, String message) {
		try {
			Send send = vk.messages.send()
					.setPeerId(chatId.intValue())
					.setMessage(message);
			send.execute();
		} catch (VkApiException e) {
			logger.error("Не удалось отправить сообщение!", e);
		}
	}

	@Override
	public void sendMessageWithRateKeyboard(Long chatId, String message) {
		try {
			Send send = vk.messages.send()
					.setPeerId(chatId.intValue())
					.setMessage(message);
			send.setKeyboard(rateKeyboard);
			send.execute();
		} catch (VkApiException e) {
			logger.error("Не удалось отправить сообщение!", e);
		}
	}

	/**
	 * Запуск бота
	 */
	public void start() {
		try {
			this.startPolling();
		} catch (VkApiException e) {
			throw new RuntimeException("Не удалось запустить бота!");
		}
	}

	/**
	 * Создание клавиатуры с оценками
	 *
	 * @return клавиатура
	 */
	private Keyboard createRateKeyboard() {
		List<List<Button>> buttons = new ArrayList<>();
		List<Button> buttonRow = new ArrayList<>();
		for (String textButton : listRate) {
			buttonRow.add(new TextButton(Button.Color.POSITIVE, new TextButton.Action(textButton)));
		}
		buttons.add(buttonRow);
		Keyboard keyboard = new Keyboard(buttons);
		keyboard.setOneTime(true);
		return keyboard;
	}
}
