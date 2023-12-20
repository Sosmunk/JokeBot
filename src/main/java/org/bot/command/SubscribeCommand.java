package org.bot.command;

import org.bot.DatabaseScheduler;
import org.bot.command.data.ChatData;
import org.bot.enumerable.ChatPlatform;

import java.time.Instant;

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

		Instant instantDate = dateTimeParser.parserArgsToDate(args);
		databaseScheduler.schedule(new ChatData(chatPlatform, chatId), instantDate);

		//TODO: Scheduling
		return "ПОКА НЕ РАБОТАЕТ";
	}
}
