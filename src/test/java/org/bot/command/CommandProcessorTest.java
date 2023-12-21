package org.bot.command;

import org.bot.Joke;
import org.bot.Rate;
import org.bot.dao.JokeDAO;
import org.bot.dao.RatingDAO;
import org.bot.dao.RatingService;
import org.bot.dao.RatingServiceImpl;
import org.bot.service.JokeService;
import org.bot.service.JokeServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Тест обработки команд
 */
public class CommandProcessorTest {
	JokeDAO mockJokeDao = Mockito.mock(JokeDAO.class);
	JokeService jokeService = new JokeServiceImpl(mockJokeDao);
	private final RatingDAO mockRatingDao = Mockito.mock(RatingDAO.class);
	private final RatingService ratingService = new RatingServiceImpl(mockRatingDao, jokeService);

	private final CommandProcessor commandProcessor = new CommandProcessor(jokeService, ratingService);

	private final Joke testJoke = new Joke(FIRST_JOKE);
	private final Joke joke2 = new Joke(SECOND_JOKE);

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
		this.joke2.setId(2);
	}

	/**
	 * Тест на неправильную команду
	 */
	@Test
	public void testRunCommandWithNull() {
		Assert.assertEquals("Команда не найдена",
				commandProcessor.runCommand(null, chatId));
		Assert.assertEquals("Команда не найдена",
				commandProcessor.runCommand("", chatId));
		Assert.assertEquals("Команда не найдена",
				commandProcessor.runCommand("/exampleCommand", chatId));
	}

	/**
	 * Тест команды /start
	 */
	@Test
	public void testStartCommand() {
		String command = "/start";
		Assert.assertEquals("Wrong message", "Привет, я бот - любитель анекдотов." +
						" Чтобы получить справку о работе со мной напишите /help.",
				commandProcessor.runCommand(command, chatId));
	}

	/**
	 * Тест команды /help
	 */
	@Test
	public void testHelpCommand() {
		String command = "/help";
		Assert.assertEquals("Wrong message", """
						Вот всё что я умею:
						                
						😂 Показать случайный анекдот (/joke)
						    
						😂🔢 Показать анекдот по номеру
						     (/getJoke <номер анекдота>)
						    
						👶🏼 Справка о командах бота (/help)
						                        
						⭐ Оценить анекдот
						   (/rate <номер анекдота> <оценка от 1 до 5>)
						""",
				commandProcessor.runCommand(command, chatId));
	}

	/**
	 * Тест команды /joke
	 */
	@Test
	public void testJokeCommand() {
		String command = "/joke";
		Mockito.when(jokeService.getRandomJoke())
				.thenReturn(testJoke);
		Assert.assertEquals("Invalid message",
				"Анекдот №1\n" + FIRST_JOKE,
				commandProcessor.runCommand(command, chatId));
	}

	/**
	 * Тест команды /getJoke &lt;id&gt;
	 */
	@Test
	public void testGetJokeCommand() {
		String command = "/getJoke 1";
		Mockito.when(jokeService.getJoke(1))
				.thenReturn(testJoke);
		Assert.assertEquals("Invalid message",
				"Анекдот №1\n" + FIRST_JOKE,
				commandProcessor.runCommand(command, chatId));
	}

	/**
	 * Тест команды /getJoke при отсутствии анекдота
	 */
	@Test
	public void getJokeNotFoundTest() {
		String command = "/getJoke 123";
		Assert.assertEquals("Анекдот не найден",
				commandProcessor.runCommand(command, chatId));
	}

	/**
	 * Тест команды /getJoke &lt;id&gt; на отсутствие анекдота с негативным id
	 */
	@Test
	public void getJokeNegativeNotFound() {
		Assert.assertEquals("Анекдот не найден",
				commandProcessor.runCommand("/getJoke -1",
						chatId));
	}

	/**
	 * Тест /getJoke при буквенных аргументах
	 */
	@Test
	public void getJokeLetterArgsTest() {
		Assert.assertEquals("""
						Неправильный номер команды! Ответ должен содержать только цифры.
						Например: "/getJoke 1"
						""",
				commandProcessor.runCommand("/getJoke AAAAAAAAA",
						chatId));
	}

	/**
	 * Тест /getJoke если не указаны аргументы команды
	 */
	@Test
	public void getJokeNoArgsTest() {
		Assert.assertEquals("Введите \"/getJoke <номер анекдота>\"",
				commandProcessor.runCommand("/getJoke",
						chatId));
	}

	/**
	 * Тест невозможности оценки несуществующего анекдота
	 */
	@Test
	public void testRateNotExistingJoke() {
		String notFound = commandProcessor.runCommand("/rate 999 5",
				chatId);
		Assert.assertEquals("Анекдот не найден", notFound);
		Mockito.verify(mockRatingDao, Mockito.never())
				.saveRating(Mockito.any());
		Mockito.verify(mockRatingDao, Mockito.never())
				.updateRating(Mockito.any(), Mockito.any(Byte.class));
	}

	/**
	 * Тест оценки существующего анекдота
	 */
	@Test
	public void testRateExistingJoke() {
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(joke2);

		String res = commandProcessor.runCommand("/rate 2 1",
				chatId);
		Assert.assertEquals("Анекдот оценен", res);
		Mockito.verify(mockRatingDao, Mockito.times(1))
				.saveRating(Mockito.any(Rate.class));
		Mockito.verify(mockRatingDao, Mockito.never())
				.updateRating(Mockito.any(), Mockito.any(Byte.class));
	}

	public void testRateCorrectPrint() {
		Mockito.when(jokeService.getJoke(2)).thenReturn(joke2);

		Mockito.when(mockRatingDao.findAverageStarsForJoke(2))
				.thenReturn(Optional.of(1.0));


		String hasRating = commandProcessor.runCommand("/getJoke 2",
				chatId);

		Assert.assertEquals("Анекдот №2\n" + joke2.getText()
						+ "\n" + "Рейтинг анекдота: 1.0",
				hasRating);

		Mockito.when(mockRatingDao.findAverageStarsForJoke(2))
				.thenReturn(Optional.of(1.5));

		String hasMultipleRatings = commandProcessor.runCommand("/getJoke 2",
				chatId);

		Assert.assertEquals("Анекдот №2\n" + joke2.getText()
						+ "\n" + "Рейтинг анекдота: 1.5",
				hasMultipleRatings);

		Mockito.when(mockRatingDao.findAverageStarsForJoke(2))
				.thenReturn(Optional.empty());

		String hasNoRatings = commandProcessor.runCommand("/getJoke 2",
				chatId);

		Assert.assertEquals("Анекдот №2\n" + joke2.getText(),
				hasNoRatings);

	}

	/**
	 * Тест /rate при неверном количество звезд рейтинга
	 */
	@Test
	public void testRateWrongCountStars() {
		String tooManyStars = commandProcessor.runCommand("/rate 2 100",
				chatId);
		Assert.assertEquals("Неверное количество звезд рейтинга",
				tooManyStars);
		String notEnoughStars = commandProcessor.runCommand("/rate 2 -1",
				chatId);
		Assert.assertEquals("Неверное количество звезд рейтинга", notEnoughStars);
	}

	/**
	 * Тест ввода неправильного типа количества звёзд рейтинга
	 */
	@Test
	public void testRateWrongTypeNumberStars() {
		String abracadabraStars = commandProcessor
				.runCommand("/rate 2 abracadabra",
						chatId);
		Assert.assertEquals(
				"Количество звезд рейтинга должно содержать только цифры (1-5)",
				abracadabraStars);
		String overByteLimitStars = commandProcessor.runCommand(
				"/rate 2 100000000",
				chatId);
		Assert.assertEquals(
				"Количество звезд рейтинга должно содержать только цифры (1-5)",
				overByteLimitStars);
	}

	/**
	 * Тест при неверном количестве аргументов
	 */
	@Test
	public void testRateIncorrectNumberArguments() {
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(joke2);

		String tooManyArgs = commandProcessor
				.runCommand("/rate 2 1 1 1 1 1 1 1", chatId);
		String noArgs = commandProcessor.runCommand("/rate ",
				chatId);
		String oneArg = commandProcessor.runCommand("/rate 2",
				chatId);
		String invalidArgs = "Неверное количество аргументов";

		Assert.assertEquals(invalidArgs, tooManyArgs);
		Assert.assertEquals(invalidArgs, noArgs);
		Assert.assertEquals(invalidArgs, oneArg);
	}

	/**
	 * Тест обновления рейтинга анекдота
	 */
	@Test
	public void testUpdateRate() {
		Rate rate = new Rate(chatId, (byte) 1, null);

		Mockito.when(jokeService.getJoke(2))
				.thenReturn(joke2);

		Mockito.when(mockRatingDao.findRating(2, chatId))
				.thenReturn(rate);

		String updateSameJoke = commandProcessor
				.runCommand("/rate 2 2", chatId);

		Assert.assertEquals("Анекдот оценен", updateSameJoke);
		Mockito.verify(mockRatingDao, Mockito.times(1))
				.updateRating(rate, (byte) 2);
	}

	/**
	 * Тестирование оценки последнего анекдота,
	 * если никогда не была вызвана команда /joke или /getJoke
	 */
	@Test
	public void testRateNoLastJoke() {
		String noLastJokes = commandProcessor.runCommand("1☆",
				chatId);
		Assert.assertEquals("Нет анекдотов для оценивания", noLastJokes);
	}

	/**
	 * Тест оценки последнего анекдота
	 */
	@Test
	public void rateLastTest() {

		Rate testRate = new Rate(chatId, (byte) 1, testJoke);
		Mockito.when(jokeService.getJoke(testJoke.getId()))
				.thenReturn(testJoke);

		commandProcessor.runCommand("/getJoke " + testJoke.getId(),
				chatId);

		String rated = commandProcessor.runCommand("1☆",
				chatId);
		Assert.assertEquals("Анекдот оценен", rated);

		Mockito.verify(mockRatingDao, Mockito.times(1))
				.saveRating(Mockito.any(Rate.class));

		Mockito.when(mockRatingDao.findRating(testJoke.getId(), chatId))
				.thenReturn(testRate);

		for (int i = 2; i <= 5; i++) {
			String update = commandProcessor.runCommand(i + "☆",
					chatId);
			Assert.assertEquals("Анекдот оценен", update);
			Assert.assertEquals(jokeService.getLastJokeId(chatId), testJoke.getId());
			Mockito.verify(mockRatingDao, Mockito.times(1))
					.updateRating(
							testRate,
							(byte) i
					);
		}
	}

	/**
	 * Тест сохранения последнего анекдота
	 */
	@Test
	public void testSaveLastJoke() {
		Mockito.when(jokeService.getJoke(1))
				.thenReturn(testJoke);

		commandProcessor.runCommand("/getJoke " + testJoke.getId(),
				chatId);
		Mockito.when(jokeService.getJoke(2))
				.thenReturn(joke2);

		commandProcessor.runCommand("/getJoke " + joke2.getId(),
				chatId);

		Assert.assertEquals(jokeService.getLastJokeId(chatId), joke2.getId());
		Assert.assertNotEquals(jokeService.getLastJokeId(chatId), testJoke.getId());
	}
}
