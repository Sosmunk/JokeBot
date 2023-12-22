package org.bot.command;

import org.bot.JokeScheduler;
import org.bot.bot.Bot;

import java.time.Instant;
import java.time.format.DateTimeParseException;

/**
 * /subscribe HH-mm <br>
 * Подписка на ежедневные анекдоты
 */
public class SubscribeCommand implements BotCommand {

	private final JokeScheduler jokeScheduler;

	private final DateTimeParser dateTimeParser = new DateTimeParser();

	public SubscribeCommand(JokeScheduler jokeScheduler) {
		this.jokeScheduler = jokeScheduler;
	}

	@Override
	public void execute(String args, Long chatId, Bot bot) {
		try {
			Instant instantDate = dateTimeParser.parserArgsToDate(args);
			jokeScheduler.schedule(bot.getChatPlatform(), chatId, instantDate);
			bot.sendMessage(chatId, "Теперь вы будете получать анекдот в " + args);
		} catch (DateTimeParseException e) {
			bot.sendMessage(chatId, "Ошибка при парсинге времени");
		}
	}
}
