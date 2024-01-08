package org.bot.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bot.bot.keyboard.KeyboardUtils;
import org.bot.command.CommandProcessor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Телеграм бот
 */
public class TelegramBot extends TelegramLongPollingBot implements Bot {
	private final CommandProcessor commandProcessor;

	/**
	 * Клавиатура с оценками для бота
	 */
	private final ReplyKeyboardMarkup rateKeyboardMarkup;
	private final Logger logger = LogManager.getLogger();
	/**
	 * Имя бота
	 */
	private final String BOT_NAME = System.getenv("TG_BOT_NAME");

	public TelegramBot(CommandProcessor commandProcessor) {
		super(System.getenv("TG_TOKEN"));
		this.commandProcessor = commandProcessor;
		this.rateKeyboardMarkup = createRateKeyboard();
	}

	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage()
				.hasText()) {
			Message textInMessage = update.getMessage();
			long chatId = textInMessage.getChatId();
			commandProcessor.runCommand(
					textInMessage.getText(),
					chatId, this);
		}
	}

	public String getBotUsername() {
		return BOT_NAME;
	}

	@Override
	public void sendMessage(Long chatId, String message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(message);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			logger.error("Не удалось отправить сообщение!", e);
		}
	}

	@Override
	public void sendMessageWithRateKeyboard(Long chatId, String message) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(message);
		try {
			sendMessage.setReplyMarkup(rateKeyboardMarkup);
			execute(sendMessage);
		} catch (TelegramApiException e) {
			logger.error("Не удалось отправить сообщение!", e);
		}
	}

	/**
	 * Запуск бота
	 */
	public void start() {
		try {
			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
			telegramBotsApi.registerBot(this);
		} catch (Exception e) {
			throw new RuntimeException("Не удалось запустить бота!");
		}
	}

	/**
	 * Создание клавиатуры с оценками
	 *
	 * @return клавиатура с оценками
	 */
	private ReplyKeyboardMarkup createRateKeyboard() {
		List<String> listRate = new KeyboardUtils().getListRates();
		ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
		List<KeyboardRow> keyboardRows = new ArrayList<>();
		KeyboardRow row = new KeyboardRow();
		for (String button : listRate) {
			row.add("\n" + button);
		}
		keyboardRows.add(row);
		keyboardMarkup.setKeyboard(keyboardRows);
		keyboardMarkup.setResizeKeyboard(true);
		keyboardMarkup.setOneTimeKeyboard(true);

		return keyboardMarkup;
	}
}