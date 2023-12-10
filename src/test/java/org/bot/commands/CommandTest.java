package org.bot.commands;

import org.bot.FakeService;
import org.bot.Joke;
import org.junit.Assert;
import org.junit.Test;

/**
 * Тест обработки команд /help, /start, /joke, /getJoke &lt;id&gt;
 */
public class CommandTest {

    private final FakeService fakeService = new FakeService();
    private final CommandProcessor commandProcessor = new CommandProcessor(fakeService);

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
                commandProcessor.runCommand(command, null));
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
                commandProcessor.runCommand(command, null));
    }

    /**
     * Тест команды /joke
     */
    @Test
    public void testJokeCommand(){
        fakeService.saveJoke(new Joke(FIRST_JOKE));

        String command = "/joke";
        Assert.assertEquals("Invalid message", String.format("Анекдот №1%n") + FIRST_JOKE,
                commandProcessor.runCommand(command, null));
    }

    /**
     * Тест команды /getJoke &lt;id>&gt;
     */
    @Test
    public void testGetJokeCommand(){
        fakeService.saveJoke(new Joke(FIRST_JOKE));

        String command = "/getJoke 1";
        Assert.assertEquals("Invalid message", String.format("Анекдот №1%n") + FIRST_JOKE,
                commandProcessor.runCommand(command, null));
    }

    /**
     * Тест команды /getJoke при отсутствии анекдота
     */
    @Test
    public void getJokeNotFoundTest() {
        String command = "/getJoke 123";
        Assert.assertEquals("Анекдот не найден", commandProcessor.runCommand(command, null));

    }
}
