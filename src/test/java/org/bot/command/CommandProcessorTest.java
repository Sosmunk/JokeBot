package org.bot.command;

import org.bot.Joke;
import org.bot.Rate;
import org.bot.dao.JokeDAO;
import org.bot.dao.RatingDAO;
import org.bot.dao.RatingService;
import org.bot.dao.RatingServiceImpl;
import org.bot.enumerable.ChatPlatform;
import org.bot.service.JokeService;
import org.bot.service.JokeServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Тест обработки команд
 */
public class CommandProcessorTest {
    JokeDAO jokeDAO = Mockito.mock(JokeDAO.class);
    JokeService jokeService = new JokeServiceImpl(jokeDAO);
    private final RatingDAO mockRatingDao = Mockito.mock(RatingDAO.class);
    private final RatingService ratingService = new RatingServiceImpl(mockRatingDao, jokeService);

    private final CommandProcessor commandProcessor = new CommandProcessor(jokeService, ratingService);

    private final Joke testJoke = new Joke(firstJoke);

    private final static String firstJoke = """
            — Заходит программист в лифт, а ему надо на 12—й этаж.
            — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
            """;

    @Before
    public void setUp() {
        this.testJoke.setId(1);
    }

    private final Long chatId = 12345L;

    private final ChatPlatform chatPlatform = ChatPlatform.TELEGRAM;


    /**
     * Тест на неправильную команду
     */
    @Test
    public void testRunCommandWithNull() {
        Assert.assertEquals("Команда не найдена",
                commandProcessor.runCommand(null, chatId, chatPlatform));
        Assert.assertEquals("Команда не найдена",
                commandProcessor.runCommand("", chatId, chatPlatform));
        Assert.assertEquals("Команда не найдена",
                commandProcessor.runCommand("/exampleCommand", chatId, chatPlatform));
    }

    /**
     * Тест команды /start
     */
    @Test
    public void testStartCommand() {
        String command = "/start";
        Assert.assertEquals("Wrong message", "Привет, я бот - любитель анекдотов." +
                        " Чтобы получить справку о работе со мной напишите /help.",
                commandProcessor.runCommand(command, chatId, chatPlatform));
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
                commandProcessor.runCommand(command, chatId, chatPlatform));
    }

    /**
     * Тест команды /joke
     */
    @Test
    public void testJokeCommand() {
        String command = "/joke";
        Mockito.when(jokeService.getRandomJoke()).thenReturn(testJoke);
        Assert.assertEquals("Invalid message",
                "Анекдот №1\n" + firstJoke,
                commandProcessor.runCommand(command, chatId, chatPlatform));
    }

    /**
     * Тест команды /getJoke &lt;id&gt;
     */
    @Test
    public void testGetJokeCommand() {
        String command = "/getJoke 1";
        Mockito.when(jokeService.getJoke(1)).thenReturn(testJoke);
        Assert.assertEquals("Invalid message",
                "Анекдот №1\n" + firstJoke,
                commandProcessor.runCommand(command, chatId, chatPlatform));
    }

    /**
     * Тест команды /getJoke при отсутствии анекдота
     */
    @Test
    public void getJokeNotFoundTest() {
        String command = "/getJoke 123";
        Assert.assertEquals("Анекдот не найден",
                commandProcessor.runCommand(command, chatId, chatPlatform));
    }

    /**
     * Тест команды /getJoke &lt;id&gt; на отсутствие анекдота с негативным id
     */
    @Test
    public void getJokeNegativeNotFound() {
        Assert.assertEquals("Анекдот не найден",
                commandProcessor.runCommand("/getJoke -1", chatId, chatPlatform));
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
                commandProcessor.runCommand("/getJoke AAAAAAAAA", chatId, chatPlatform));
    }

    /**
     * Тест /getJoke если не указаны аргументы команды
     */
    @Test
    public void getJokeNoArgsTest() {
        Assert.assertEquals("Введите \"/getJoke <номер анекдота>\"",
                commandProcessor.runCommand("/getJoke", chatId, chatPlatform));
    }

    /**
     * Тестирование команды /rate
     */
    @Test
    public void rateJokeTest() {
        Joke joke = new Joke("aaaa");
        joke.setId(2);
        jokeService.saveJoke(joke);
        Mockito.when(jokeService.getJoke(2)).thenReturn(joke);

        // Оценка анекдота которого не существует
        String notFound = commandProcessor.runCommand("/rate 999 5", chatId, chatPlatform);
        Assert.assertEquals("Анекдот не найден", notFound);
        Mockito.verify(mockRatingDao, Mockito.never()).saveRating(Mockito.any());
        Mockito.verify(mockRatingDao, Mockito.never()).updateRating(Mockito.any(), Mockito.any(Byte.class));

        // Оценка анекдота который существует
        String res = commandProcessor.runCommand("/rate 2 1", chatId, chatPlatform);
        Assert.assertEquals("Анекдот оценен", res);
        Mockito.verify(mockRatingDao, Mockito.times(1))
                .saveRating(Mockito.any(Rate.class));
        Mockito.verify(mockRatingDao, Mockito.never()).updateRating(Mockito.any(), Mockito.any(Byte.class));

        // Корректный вывод рейтинга анекдота
        joke.setRatings(List.of(new Rate(chatId, (byte) 1, joke)));
        String hasRating = commandProcessor.runCommand("/getJoke 2", chatId, chatPlatform);
        Assert.assertEquals("Анекдот №2\n" + joke.getText() + "\n" + "Рейтинг анекдота: 1.0", hasRating);
        joke.setRatings(List.of(new Rate(chatId, (byte) 1, joke), new Rate(123L, (byte) 3, joke)));
        String hasMultipleRatings = commandProcessor.runCommand("/getJoke 2", chatId, chatPlatform);
        Assert.assertEquals("Анекдот №2\n" + joke.getText() + "\n" + "Рейтинг анекдота: 2.0",
                hasMultipleRatings);

        // 100 звезд при ограничении 1-5
        String tooManyStars = commandProcessor.runCommand("/rate 2 100", chatId, chatPlatform);
        Assert.assertEquals("Неверное количество звезд рейтинга", tooManyStars);
        String notEnoughStars = commandProcessor.runCommand("/rate 2 -1", chatId, chatPlatform);
        Assert.assertEquals("Неверное количество звезд рейтинга", notEnoughStars);

        // Указано не byte количество звезд
        String abracadabraStars = commandProcessor
                .runCommand("/rate 2 abracadabra",
                        chatId,
                        null);
        Assert.assertEquals("Количество звезд рейтинга должно содержать только цифры (1-5)", abracadabraStars);
        String overByteLimitStars = commandProcessor.runCommand("/rate 2 100000000", chatId, chatPlatform);
        Assert.assertEquals("Количество звезд рейтинга должно содержать только цифры (1-5)", overByteLimitStars);


        // Неверное количество аргументов
        String tooManyArgs = commandProcessor
                .runCommand("/rate 2 1 1 1 1 1 1 1", chatId, chatPlatform);
        String noArgs = commandProcessor.runCommand("/rate ", chatId, chatPlatform);
        String oneArg = commandProcessor.runCommand("/rate 2", chatId, chatPlatform);
        String invalidArgs = "Неверное количество аргументов";

        Assert.assertEquals(invalidArgs, tooManyArgs);
        Assert.assertEquals(invalidArgs, noArgs);
        Assert.assertEquals(invalidArgs, oneArg);

        // Обновление рейтинга анекдота
        Mockito.when(mockRatingDao.findRating(2, chatId))
                .thenReturn(new Rate(chatId, (byte) 1, null));

        String updateSameJoke = commandProcessor
                .runCommand("/rate 2 2", chatId, chatPlatform);

        Assert.assertEquals("Анекдот оценен", updateSameJoke);
        Mockito.verify(mockRatingDao, Mockito.times(1))
                .updateRating(Mockito.any(Rate.class), Mockito.any(Byte.class));

    }

    @Test
    public void rateLastTest() {

        String noLastJokes = commandProcessor.runCommand("1☆", chatId, chatPlatform);
        Assert.assertEquals("Нет анекдотов для оценивания", noLastJokes);
        Rate testRate = new Rate(chatId, (byte) 1, testJoke);
        Mockito.when(jokeService.getJoke(testJoke.getId())).thenReturn(testJoke);

        commandProcessor.runCommand("/getJoke " + testJoke.getId(), chatId, chatPlatform);


        String rated = commandProcessor.runCommand("1☆", chatId, chatPlatform);
        Assert.assertEquals("Анекдот оценен", rated);

        Mockito.verify(mockRatingDao, Mockito.times(1))
                .saveRating(Mockito.any(Rate.class));

        Mockito.when(mockRatingDao.findRating(testJoke.getId(), chatId)).thenReturn(testRate);

        for (int i = 2; i < 6; i++) {
            String update = commandProcessor.runCommand(i + "☆", chatId, chatPlatform);
            Assert.assertEquals("Анекдот оценен", update);
            Mockito.verify(mockRatingDao, Mockito.times(i - 1))
                    .updateRating(
                            Mockito.any(Rate.class),
                            Mockito.any(byte.class)
                    );
        }
    }
}
