package org.bot.command;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Парсер аргументов /subscribe &lt;HH:mm&gt;
 */
public class DateTimeParser {
	/**
	 * Возвращает дату по полученному сообщению
	 *
	 * @param args сообщение пользователя
	 * @return Date указанного времени
	 */
	public Instant parserArgsToDate(String args) {
		if (args == null) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime localTime = LocalTime.parse(args, formatter);
		LocalDate localDate = LocalDate.now(Clock.systemUTC());
		LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
		return localDateTime.toInstant(ZoneOffset.of("+5"));
	}
}
