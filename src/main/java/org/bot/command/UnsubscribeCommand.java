package org.bot.command;

import org.bot.JokeScheduler;
import org.bot.bot.Bot;

/**
 * /unsubscribe <br>
 * Отписаться от ежедневных анекдотов
 */
public class UnsubscribeCommand implements BotCommand {

	private final JokeScheduler jokeScheduler;

	public UnsubscribeCommand(JokeScheduler jokeScheduler) {
		this.jokeScheduler = jokeScheduler;
	}

	@Override
	public void execute(String args, Long chatId, Bot bot) {
		this.jokeScheduler.deschedule(bot.getChatPlatform(), chatId);
		bot.sendMessage(chatId, "Теперь вы не будете получать ежедневные анекдоты");
	}
}
