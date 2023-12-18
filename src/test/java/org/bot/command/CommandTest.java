package org.bot.command;

import org.bot.FakeJokeService;
import org.bot.Joke;
import org.junit.Assert;
import org.junit.Test;

/**
 * Тест обработки команд /help, /start, /joke, /getJoke &lt;id&gt;
 */
public class CommandTest {

    private final FakeJokeService fakeJokeService = new FakeJokeService();
    private final CommandProcessor commandProcessor = new CommandProcessor(fakeJokeService);

    private final String FIRST_JOKE = """
            — Заходит программист в лифт, а ему надо на 12—й этаж.
            — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
            """;

    /**
     * Тест на неправильную команду
     */
    @Test
    public void testRunCommandWithNull() {
        Assert.assertEquals("Команда не найдена",
                commandProcessor.runCommand(null));
        Assert.assertEquals("Команда не найдена",
                commandProcessor.runCommand(""));
        Assert.assertEquals("Команда не найдена",
                commandProcessor.runCommand("/exampleCommand"));
    }

    /**
     * Тест команды /start
     */
    @Test
    public void testStartCommand() {
        String command = "/start";
        Assert.assertEquals("Wrong message", "Привет, я бот - любитель анекдотов." +
                        " Чтобы получить справку о работе со мной напишите /help.",
                commandProcessor.runCommand(command));
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
                commandProcessor.runCommand(command));
    }

    /**
     * Тест команды /joke
     */
    @Test
    public void testJokeCommand(){
        fakeJokeService.saveJoke(new Joke(FIRST_JOKE));

        String command = "/joke";
        Assert.assertEquals("Invalid message",
                "Анекдот №1\n" + FIRST_JOKE,
                commandProcessor.runCommand(command));
    }

    /**
     * Тест команды /getJoke &lt;id&gt;
     */
    @Test
    public void testGetJokeCommand(){
        fakeJokeService.saveJoke(new Joke(FIRST_JOKE));

        String command = "/getJoke 1";
        Assert.assertEquals("Invalid message",
                "Анекдот №1\n" + FIRST_JOKE,
                commandProcessor.runCommand(command));
    }

    /**
     * Тест команды /getJoke при отсутствии анекдота
     */
    @Test
    public void getJokeNotFoundTest() {
        String command = "/getJoke 123";
        Assert.assertEquals("Анекдот не найден",
                commandProcessor.runCommand(command));
    }

    /**
     * Тест команды /getJoke &lt;id&gt; на отсутствие анекдота с негативным id
     */
    @Test
    public void getJokeNegativeNotFound() {
        Assert.assertEquals("Анекдот не найден",
                commandProcessor.runCommand("/getJoke -1"));
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
                commandProcessor.runCommand("/getJoke AAAAAAAAA"));
    }

    /**
     * Тест /getJoke если не указаны аргументы команды
     */
    @Test
    public void getJokeNoArgsTest() {
        Assert.assertEquals("Введите \"/getJoke <номер анекдота>\"",
                commandProcessor.runCommand("/getJoke"));
    }


}