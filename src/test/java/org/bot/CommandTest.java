package org.bot;

import org.bot.commands.CommandProcessor;
import org.bot.dto.CommandData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Класс для тестирования обработки команд
 */
public class CommandTest {

    private final FakeService fakeService = new FakeService();
    CommandProcessor commandProcessor = new CommandProcessor(fakeService);

    /**
     * Тест команды /start
     */
    @Test
    public void testStartCommand(){
        CommandData commandData = new CommandData("/start",null);
        Assert.assertEquals("Wrong message", "Привет, я бот - любитель анекдотов." +
                        " Чтобы получить справку о работе со мной напишите /help.",
                commandProcessor.runCommand(commandData));
    }

    /**
     * Тест команды /help
     */
    @Test
    public void testHelpCommand(){
        CommandData commandData = new CommandData("/help",null);
        Assert.assertEquals("Wrong message", """
                        Вот всё что я умею:
                                        
                        😂 Показать случайный анекдот (/joke)
                            
                        😂🔢 Показать анекдот по номеру
                             (/getJoke <номер анекдота>)
                            
                        👶🏼 Справка о командах бота (/help)
                        """,
                commandProcessor.runCommand(commandData));
    }

    /**
     * Тест команды /joke
     */
    @Test
    public void testJokeCommand(){
        fakeService.saveJoke(new Joke("""
                — Заходит программист в лифт, а ему надо на 12—й этаж.
                — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                """));

        CommandData commandData = new CommandData("/joke",null);
        Assert.assertEquals("Invalid message", String.format("Анекдот №1%n") + """
                        — Заходит программист в лифт, а ему надо на 12—й этаж.
                        — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                        """,
                commandProcessor.runCommand(commandData));
    }

    /**
     * Тест команды /getJoke <id>
     */
    @Test
    public void testGetJokeCommand(){
        fakeService.saveJoke(new Joke("""
                — Заходит программист в лифт, а ему надо на 12—й этаж.
                — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                """));

        CommandData commandData = new CommandData("/getJoke", "1");
        Assert.assertEquals("Invalid message", String.format("Анекдот №1%n") + """
                        — Заходит программист в лифт, а ему надо на 12—й этаж.
                        — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                        """,
                commandProcessor.runCommand(commandData));
    }

    /**
     * Тест команды /getJoke при отсутствии анекдота
     */
    @Test
    public void getJokeNotFoundTest() {
        CommandData getJokecommandData = new CommandData("/getJoke", "123");
        Assert.assertEquals("Анекдот не найден", commandProcessor.runCommand(getJokecommandData));

    }
}
