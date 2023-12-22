package org.bot.command;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Тест DateTimeParser
 * #{@link DateTimeParser}
 */
public class DateTimeParserTest {

	private final DateTimeParser dateTimeParser = new DateTimeParser();

	public DateTimeParserTest() {

	}

	/**
	 * Тест совпадения даты с использованием парсера
	 */
	@Test
	public void testCorrectDate() {
		String date = "20:30";
		Instant instant = Instant.now();
		instant = instant.atZone(ZoneOffset.UTC)
				.withHour(20)
				.withMinute(30)
				.withSecond(35)
				.truncatedTo(ChronoUnit.MINUTES)
				.toInstant();
		Assert.assertEquals(instant, dateTimeParser.parserArgsToDate(date));
	}

	/**
	 * Тест парсера на возврат null при передачи пустой строки
	 */
	@Test
	public void testNoArgs() {
		String date = "";
		Assert.assertNull(dateTimeParser.parserArgsToDate(date));
	}
}