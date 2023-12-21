package org.bot.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bot.bot.keyboard.TgKeyboard;
import org.bot.command.CommandProcessor;
import org.bot.enumerable.ChatPlatform;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Телеграм бот
 */
public class TelegramBot extends TelegramLongPollingBot implements Bot {
	private final CommandProcessor commandProcessor;
	private final ReplyKeyboardMarkup replyKeyboardMarkup = new TgKeyboard().createKeyboard();
	private final Logger logger = LogManager.getLogger();

	/**
	 * Имя бота
	 */
	private final String BOT_NAME = System.getenv("TG_BOT_NAME");

	public TelegramBot(CommandProcessor commandProcessor) {
		super(System.getenv("TG_TOKEN"));
		this.commandProcessor = commandProcessor;
	}

	/**
	 * Метод, который получает сообщение от пользователя и отправляет ему новое в ответ
	 *
	 * @param update обновление из api
	 */
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage()
				.hasText()) {
			Message textInMessage = update.getMessage();
			long chatId = textInMessage.getChatId();
			String result = commandProcessor.runCommand(
					textInMessage.getText(),
					chatId,
					ChatPlatform.TELEGRAM);
			sendMessage(chatId, result);
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
			if (message.contains("Анекдот №")) {
				sendMessage.setReplyMarkup(replyKeyboardMarkup);
			}
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
}