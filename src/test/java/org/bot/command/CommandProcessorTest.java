package org.bot.command;

import org.bot.Joke;
import org.bot.JokeScheduler;
import org.bot.Rate;
import org.bot.bot.FakeBot;
import org.bot.dao.JokeDAO;
import org.bot.dao.RatingDAO;
import org.bot.service.JokeService;
import org.bot.service.JokeServiceImpl;
import org.bot.service.RatingService;
import org.bot.service.RatingServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Тест обработки команд
 */
public class CommandProcessorTest {
	JokeDAO mockJokeDao = Mockito.mock(JokeDAO.class);
	JokeService jokeService = new JokeServiceImpl(mockJokeDao);
	private final RatingDAO mockRatingDao = Mockito.mock(RatingDAO.class);
	private final RatingService ratingService = new RatingServiceImpl(mockRatingDao, jokeService);

	private final CommandProcessor commandProcessor = new CommandProcessor(jokeService, ratingService);

	private final JokeScheduler mockScheduler = Mockito.mock(JokeScheduler.class);

	private final FakeBot fakeBot = new FakeBot();

	private final Joke testJoke = new Joke(FIRST_JOKE);
	private final Joke secondTestJoke = new Joke(SECOND_JOKE);

	private final static String FIRST_JOKE = """
			— Заходит программист в лифт, а ему надо на 12—й этаж.
			— Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
			""";
	private final static String SECOND_JOKE = """
			Разработчики, обвиненные в написании нечитабельного кода, отказались давать комментарии
			""";

	private final Long chatId = 12345L;


	public CommandProcessorTest() {
		this.testJoke.setId(1);
		this.secondTestJoke.setId(2);
		commandProcessor.enableJokeSchedulingForBots(mockScheduler);
	}

	/**
	 * Тест на неправильную команду
	 */
	@Test
	public void testRunCommandWithNull() {
		commandProcessor.runCommand(null, chatId, fakeBot);
		Assert.assertEquals("Команда не найдена",
				fakeBot.getLastMessageText());
		commandProcessor.runCommand("", chatId, fakeBot);
		Assert.assertEquals("Команда не найдена",
				fakeBot.getLastMessageText());
		commandProcessor.runCommand("/exampleCommand", chatId, fakeBot);
		Assert.assertEquals("Команда не найдена",
				fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест команды /start
	 */
	@Test
	public void testStartCommand() {
		String command = "/start";
		commandProcessor.runCommand(command, chatId, fakeBot);
		Assert.assertEquals("Wrong message", "Привет, я бот - любитель анекдотов." +
						" Чтобы получить справку о работе со мной напишите /help.",
				fakeBot.getLastMessageText()
		);
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест команды /help
	 */
	@Test
	public void testHelpCommand() {
		String command = "/help";
		commandProcessor.runCommand(command, chatId, fakeBot);
		Assert.assertEquals("Wrong message", """
				Вот всё что я умею:

				😂 Показать случайный анекдот (/joke)

				😂🔢 Показать анекдот по номеру
				     (/getJoke <номер анекдота>)

				👶🏼 Справка о командах бота (/help)

				⭐ Оценить анекдот
				   (/rate <номер анекдота> <оценка от 1 до 5>)
				""", fakeBot.getLastMessageText()
		);
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест команды /joke
	 */
	@Test
	public void testJokeCommand() {
		String command = "/joke";
		Mockito.when(jokeService.getRandomJoke())
				.thenReturn(testJoke);
		commandProcessor.runCommand(command, chatId, fakeBot);
		Assert.assertEquals("Invalid message",
				"Анекдот №1\n" + FIRST_JOKE,
				fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
		Assert.assertTrue(fakeBot.getKeyboardPresent());
	}

	/**
	 * Тест команды /getJoke &lt;id&gt;
	 */
	@Test
	public void testGetJokeCommand() {
		String command = "/getJoke 1";
		Mockito.when(jokeService.getJoke(1))
				.thenReturn(testJoke);
		commandProcessor.runCommand(command, chatId, fakeBot);
		Assert.assertEquals("Invalid message",
				"Анекдот №1\n" + FIRST_JOKE,
				fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
		Assert.assertTrue(fakeBot.getKeyboardPresent());
	}

	/**
	 * Тест команды /getJoke при отсутствии анекдота
	 */
	@Test
	public void getJokeNotFoundTest() {
		String command = "/getJoke 123";
		commandProcessor.runCommand(command, chatId, fakeBot);
		Assert.assertEquals("Анекдот не найден",
				fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
		Assert.assertFalse(fakeBot.getKeyboardPresent());
	}

	/**
	 * Тест команды /getJoke &lt;id&gt; на отсутствие анекдота с негативным id
	 */
	@Test
	public void getJokeNegativeNotFound() {
		commandProcessor.runCommand("/getJoke -1", chatId, fakeBot);
		Assert.assertEquals("Анекдот не найден", fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
		Assert.assertFalse(fakeBot.getKeyboardPresent());
	}

	/**
	 * Тест /getJoke при буквенных аргументах
	 */
	@Test
	public void getJokeLetterArgsTest() {
		commandProcessor.runCommand("/getJoke AAAAAAAAA", chatId, fakeBot);
		Assert.assertEquals("""
				Неправильный номер команды! Ответ должен содержать только цифры.
				Например: "/getJoke 1"
				""", fakeBot.getLastMessageText()
		);
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
		Assert.assertFalse(fakeBot.getKeyboardPresent());
	}

	/**
	 * Тест /getJoke если не указаны аргументы команды
	 */
	@Test
	public void getJokeNoArgsTest() {
		commandProcessor.runCommand("/getJoke", chatId, fakeBot);
		Assert.assertEquals("Введите \"/getJoke <номер анекдота>\"",
				fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
		Assert.assertFalse(fakeBot.getKeyboardPresent());
	}

	/**
	 * Тест невозможности оценки несуществующего анекдота
	 */
	@Test
	public void testRateNotExistingJoke() {
		commandProcessor.runCommand("/rate 999 5",
				chatId, fakeBot);
		Assert.assertEquals("Анекдот не найден", fakeBot.getLastMessageText());
		Mockito.verify(mockRatingDao, Mockito.never())
				.saveRating(Mockito.any());
		Mockito.verify(mockRatingDao, Mockito.never())
				.updateRating(Mockito.any(), Mockito.any(Byte.class));
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест оценки существующего анекдота
	 */
	@Test
	public void testRateExistingJoke() {
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(secondTestJoke);

		commandProcessor.runCommand("/rate 2 1", chatId, fakeBot);
		Assert.assertEquals("Анекдот оценен", fakeBot.getLastMessageText());
		Mockito.verify(mockRatingDao, Mockito.times(1))
				.saveRating(Mockito.any(Rate.class));
		Mockito.verify(mockRatingDao, Mockito.never())
				.updateRating(Mockito.any(), Mockito.any(Byte.class));
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест правильного вывода
	 */
	@Test
	public void testRateCorrectPrint() {
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(secondTestJoke);

		Mockito.when(mockRatingDao.findAverageStarsForJoke(2))
				.thenReturn(Optional.of(1.0));


		commandProcessor.runCommand("/getJoke 2", chatId, fakeBot);

		Assert.assertEquals("Анекдот №2\n" + secondTestJoke.getText()
						+ "\n" + "Рейтинг анекдота: 1.0",
				fakeBot.getLastMessageText());

		Mockito.when(mockRatingDao.findAverageStarsForJoke(2))
				.thenReturn(Optional.of(1.5));

		commandProcessor.runCommand("/getJoke 2", chatId, fakeBot);

		Assert.assertEquals("Анекдот №2\n" + secondTestJoke.getText()
						+ "\n" + "Рейтинг анекдота: 1.5",
				fakeBot.getLastMessageText());

		Mockito.when(mockRatingDao.findAverageStarsForJoke(2))
				.thenReturn(Optional.empty());

		commandProcessor.runCommand("/getJoke 2", chatId, fakeBot);

		Assert.assertEquals("Анекдот №2\n" + secondTestJoke.getText(),
				fakeBot.getLastMessageText());

		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());

	}

	/**
	 * Тест /rate при неверном количество звезд рейтинга
	 */
	@Test
	public void testRateWrongCountStars() {
		commandProcessor.runCommand("/rate 2 100", chatId, fakeBot);
		Assert.assertEquals("Неверное количество звезд рейтинга",
				fakeBot.getLastMessageText());
		commandProcessor.runCommand("/rate 2 -1",
				chatId, fakeBot);
		Assert.assertEquals("Неверное количество звезд рейтинга", fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест ввода неправильного типа количества звёзд рейтинга
	 */
	@Test
	public void testRateWrongTypeNumberStars() {
		commandProcessor.runCommand("/rate 2 abracadabra", chatId, fakeBot);
		Assert.assertEquals(
				"Количество звезд рейтинга должно содержать только цифры (1-5)",
				fakeBot.getLastMessageText());

		commandProcessor.runCommand("/rate 2 100000000", chatId, fakeBot);
		Assert.assertEquals(
				"Количество звезд рейтинга должно содержать только цифры (1-5)",
				fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест при неверном количестве аргументов
	 */
	@Test
	public void testRateIncorrectNumberArguments() {
		String invalidArgs = "Неверное количество аргументов";
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(secondTestJoke);

		commandProcessor.runCommand("/rate 2 1 1 1 1 1 1 1", chatId, fakeBot);
		Assert.assertEquals(invalidArgs, fakeBot.getLastMessageText());
		commandProcessor.runCommand("/rate ", chatId, fakeBot);
		Assert.assertEquals(invalidArgs, fakeBot.getLastMessageText());
		commandProcessor.runCommand("/rate 2", chatId, fakeBot);
		Assert.assertEquals(invalidArgs, fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест обновления рейтинга анекдота
	 */
	@Test
	public void testUpdateRate() {
		Rate rate = new Rate(chatId, (byte) 1, null);

		Mockito.when(jokeService.getJoke(2))
				.thenReturn(secondTestJoke);

		Mockito.when(mockRatingDao.findRating(2, chatId))
				.thenReturn(rate);

		commandProcessor.runCommand("/rate 2 2", chatId, fakeBot);

		Assert.assertEquals("Анекдот оценен", fakeBot.getLastMessageText());
		Mockito.verify(mockRatingDao, Mockito.times(1))
				.updateRating(rate, (byte) 2);
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тестирование оценки последнего анекдота,
	 * если никогда не была вызвана команда /joke или /getJoke
	 */
	@Test
	public void testRateNoLastJoke() {
		commandProcessor.runCommand("1☆", chatId, fakeBot);
		Assert.assertEquals("Нет анекдотов для оценивания", fakeBot.getLastMessageText());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест оценки последнего анекдота
	 */
	@Test
	public void rateLastTest() {

		Rate testRate = new Rate(chatId, (byte) 1, testJoke);
		Mockito.when(jokeService.getJoke(testJoke.getId()))
				.thenReturn(testJoke);
		commandProcessor.runCommand("/getJoke " + testJoke.getId(), chatId, fakeBot);
		commandProcessor.runCommand("1☆", chatId, fakeBot);
		Assert.assertEquals("Анекдот оценен", fakeBot.getLastMessageText());

		Mockito.verify(mockRatingDao, Mockito.times(1))
				.saveRating(Mockito.any(Rate.class));

		Mockito.when(mockRatingDao.findRating(testJoke.getId(), chatId))
				.thenReturn(testRate);

		for (int i = 2; i <= 5; i++) {
			commandProcessor.runCommand(i + "☆", chatId, fakeBot);
			Assert.assertEquals("Анекдот оценен", fakeBot.getLastMessageText());
			Assert.assertEquals(jokeService.getLastJokeId(chatId), testJoke.getId());
			Mockito.verify(mockRatingDao, Mockito.times(1))
					.updateRating(
							testRate,
							(byte) i
					);
		}
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест сохранения последнего анекдота
	 */
	@Test
	public void testSaveLastJoke() {
		Mockito.when(jokeService.getJoke(1))
				.thenReturn(testJoke);

		commandProcessor.runCommand("/getJoke " + testJoke.getId(),
				chatId,
				fakeBot);
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(secondTestJoke);

		commandProcessor.runCommand("/getJoke " + secondTestJoke.getId(),
				chatId,
				fakeBot);

		Assert.assertEquals(jokeService.getLastJokeId(chatId), secondTestJoke.getId());
		Assert.assertNotEquals(jokeService.getLastJokeId(chatId), testJoke.getId());
		Assert.assertEquals(chatId, fakeBot.getLastMessageChatId());
	}

	/**
	 * Тест на команду /subscribe
	 */
	// К сожалению отложенную отправку тестировать не получится, так как она тесно связана с БД.
	@Test
	public void testSubscribe() {

		commandProcessor.runCommand("/subscribe 12:00", chatId, fakeBot);

		Mockito.verify(mockScheduler).schedule(fakeBot.getChatPlatform(), chatId,
				Instant.now().atZone(ZoneOffset.of("+5"))
						.withHour(12)
						.withMinute(0)
						.truncatedTo(ChronoUnit.MINUTES)
						.toInstant());
		Assert.assertEquals("Теперь вы будете получать анекдот в 12:00", fakeBot.getLastMessageText());

		commandProcessor.runCommand("/subscribe aaaaaa", chatId, fakeBot);
		Assert.assertEquals("Ошибка при парсинге времени", fakeBot.getLastMessageText());

		commandProcessor.runCommand("/subscribe 33:33", chatId, fakeBot);
		Assert.assertEquals("Ошибка при парсинге времени", fakeBot.getLastMessageText());

		commandProcessor.runCommand("/subscribe 8:00", chatId, fakeBot);
		Assert.assertEquals("Ошибка при парсинге времени", fakeBot.getLastMessageText());


	}

	/**
	 * Тест команды /unsubscribe
	 */
	@Test
	public void testUnsubscribe() {
		commandProcessor.runCommand("/unsubscribe", chatId, fakeBot);
		Assert.assertEquals("Теперь вы не будете получать ежедневные анекдоты", fakeBot.getLastMessageText());
	}
}
