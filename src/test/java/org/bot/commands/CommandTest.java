package org.bot.commands;

import org.bot.FakeService;
import org.bot.Joke;
import org.bot.Rate;
import org.bot.dao.RatingDAO;
import org.bot.dao.RatingService;
import org.bot.dao.RatingServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Тест обработки команд /help, /start, /joke, /getJoke &lt;id&gt;
 */
public class CommandTest {

    private final FakeService fakeService = new FakeService();

    private final RatingDAO mockRatingDao = Mockito.mock(RatingDAO.class);
    private final RatingService ratingService = new RatingServiceImpl(mockRatingDao, fakeService);

    private final CommandProcessor commandProcessor = new CommandProcessor(fakeService, ratingService);

    private final String FIRST_JOKE = """
            — Заходит программист в лифт, а ему надо на 12—й этаж.
            — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
            """;

    /**
     * Тест команды /start
     */
    @Test
    public void testStartCommand() {
        String command = "/start";
        Assert.assertEquals("Wrong message", "Привет, я бот - любитель анекдотов." +
                        " Чтобы получить справку о работе со мной напишите /help.",
                commandProcessor.runCommand(command, null, null));
    }

    /**
     * Тест команды /help
     */
    @Test
    public void testHelpCommand(){
        String command = "/help";
        Assert.assertEquals("Wrong message", """
                        Вот всё что я умею:
                                        
                        😂 Показать случайный анекдот (/joke)
                            
                        😂🔢 Показать анекдот по номеру
                             (/getJoke <номер анекдота>)
                            
                        👶🏼 Справка о командах бота (/help)
                        """,
                commandProcessor.runCommand(command, null, null));
    }

    /**
     * Тест команды /joke
     */
    @Test
    public void testJokeCommand(){
        fakeService.saveJoke(new Joke(FIRST_JOKE));

        String command = "/joke";
        Assert.assertEquals("Invalid message", String.format("Анекдот №1%n") + FIRST_JOKE,
                commandProcessor.runCommand(command, null, null));
    }

    /**
     * Тест команды /getJoke &lt;id>&gt;
     */
    @Test
    public void testGetJokeCommand(){
        fakeService.saveJoke(new Joke(FIRST_JOKE));

        String command = "/getJoke 1";
        Assert.assertEquals("Invalid message", String.format("Анекдот №1%n") + FIRST_JOKE,
                commandProcessor.runCommand(command, null, null));
    }

    /**
     * Тест команды /getJoke при отсутствии анекдота
     */
    @Test
    public void getJokeNotFoundTest() {
        String command = "/getJoke 123";
        Assert.assertEquals("Анекдот не найден", commandProcessor.runCommand(command, null, null));

    }

    /**
     * Тестирование команды /rate
     */
    @Test
    public void rateJokeTest() {
        Joke joke = new Joke("aaaa");
        joke.setId(1);
        fakeService.saveJoke(joke);

        // Оценка анекдота которого не существует
        String notFound = commandProcessor.runCommand("/rate 999 5", 1L, null);
        Assert.assertEquals("Анекдот не найден", notFound);
        Mockito.verify(mockRatingDao, Mockito.never()).saveRating(Mockito.any());
        Mockito.verify(mockRatingDao, Mockito.never()).updateRating(Mockito.any(), Mockito.any(Byte.class));

        // Оценка анекдота который существует
        String res = commandProcessor.runCommand("/rate 1 1", 1L, null);
        Assert.assertEquals("Анекдот оценен", res);
        Mockito.verify(mockRatingDao, Mockito.times(1))
                .saveRating(Mockito.any(Rate.class));
        Mockito.verify(mockRatingDao, Mockito.never()).updateRating(Mockito.any(), Mockito.any(Byte.class));

        // 100 звезд при ограничении 1-5
        String tooManyStars = commandProcessor.runCommand("/rate 1 100", 1L, null);
        Assert.assertEquals("Неверное количество звезд рейтинга", tooManyStars);
        String notEnoughStars = commandProcessor.runCommand("/rate 1 -1", 1L, null);
        Assert.assertEquals("Неверное количество звезд рейтинга", notEnoughStars);

        // Указано не byte количество звезд
        String abracadabraStars = commandProcessor
                .runCommand("/rate 1 abracadabra",
                        1L,
                        null);
        Assert.assertEquals("Количество звезд рейтинга должно содержать только цифры (1-5)", abracadabraStars);
        String overByteLimitStars = commandProcessor.runCommand("/rate 1 100000000", 1L, null);
        Assert.assertEquals("Количество звезд рейтинга должно содержать только цифры (1-5)", overByteLimitStars);


        // Неверное количество аргументов
        String tooManyArgs = commandProcessor
                .runCommand("/rate 1 1 1 1 1 1 1 1", 1L, null);
        String noArgs = commandProcessor.runCommand("/rate ", 1L, null);
        String oneArg = commandProcessor.runCommand("/rate 1", 1L, null);
        String invalidArgs = "Неверное количество аргументов";

        Assert.assertEquals(invalidArgs, tooManyArgs);
        Assert.assertEquals(invalidArgs, noArgs);
        Assert.assertEquals(invalidArgs, oneArg);

        // Обновление рейтинга анекдота
        Mockito.when(mockRatingDao.findRating(1, 1L))
                .thenReturn(new Rate(1L, (byte) 1, null));

        String updateSameJoke = commandProcessor
                .runCommand("/rate 1 2", 1L, null);

        Assert.assertEquals("Анекдот оценен", updateSameJoke);
        Mockito.verify(mockRatingDao, Mockito.times(1))
                .updateRating(Mockito.any(Rate.class), Mockito.any(Byte.class));

    }

    @Test
    public void rateLastTest() {
        //TODO: тесты
    }
}
