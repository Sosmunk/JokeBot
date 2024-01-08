package org.bot.command;

import org.bot.bot.Bot;
import org.bot.service.RatingService;

/**
 * Команда /rate &lt;id шутки&gt; &lt;оценка&gt; <br>
 * Оценить анекдот и отправить сообщение о статусе выполнения команды
 */
public class RateCommand implements BotCommand {

	private final RatingService ratingService;
	private static final String INVALID_ARGUMENT_COUNT = "Неверное количество аргументов";

	public RateCommand(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@Override
	public void execute(String args, Long chatId, Bot bot) {
		if (args == null) {
			bot.sendMessage(chatId, INVALID_ARGUMENT_COUNT);
			return;
		}
		String[] data = args.split(" ");
		if (data.length != 2) {
			bot.sendMessage(chatId, INVALID_ARGUMENT_COUNT);
			return;
		}

		Integer jokeId = Integer.parseInt(data[0]);
		byte stars;
		try {
			stars = Byte.parseByte(data[1]);
		} catch (NumberFormatException e) {
			bot.sendMessage(chatId,
					"Количество звезд рейтинга должно содержать только цифры (1-5)");
			return;
		}


		if (stars > 5 || stars < 1) {
			bot.sendMessage(chatId, "Неверное количество звезд рейтинга");
			return;
		}

		boolean result = this.ratingService.rateJoke(jokeId, chatId, stars);
		if (result) {
			bot.sendMessage(chatId, "Анекдот оценен");
		} else {
			bot.sendMessage(chatId, "Анекдот не найден");
		}
	}
}
