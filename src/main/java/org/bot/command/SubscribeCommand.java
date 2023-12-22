package org.bot.command;

import org.bot.DatabaseScheduler;
import org.bot.command.data.ChatData;
import org.bot.enumerable.ChatPlatform;

import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * /subscribe HH-mm <br>
 * Подписка на ежедневные анекдоты
 */
public class SubscribeCommand implements BotCommand {
	private final DatabaseScheduler databaseScheduler;
	private final DateTimeParser dateTimeParser = new DateTimeParser();

	public SubscribeCommand(DatabaseScheduler databaseScheduler) {
		this.databaseScheduler = databaseScheduler;
	}

	@Override
	public String execute(String args, Long chatId, ChatPlatform chatPlatform) {

		if (args == null) {
			return "Не указаны аргументы команды";
		}

		try {
			Instant instantDate = dateTimeParser.parserArgsToDate(args);
			databaseScheduler.schedule(new ChatData(chatPlatform, chatId), instantDate);
			return "Теперь вы будете получать анекдот в " + args;
		} catch (DateTimeParseException e) {
			return "Ошибка при парсинге времени";
		}
	}
}
