package org.bot;

import org.bot.commands.CommandProcessor;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;
import org.bot.dto.CommandData;
import org.junit.Assert;
import org.junit.Test;

public class CommandTest {
    FakeService fakeService = new FakeService();

    /**
     * Тест команды /start
     */
    @Test
    public void testStartCommand(){
        CommandProcessor commandProcessor = new CommandProcessor(null);
        CommandData commandData = new CommandData("/start",null);
        Assert.assertEquals("Wrong message","Привет, я бот - любитель анекдотов." +
                " Чтобы получить справку о работе со мной напишите /help.",
                commandProcessor.runCommand(commandData));
    }

    /**
     * Тест команды /help
     */
    @Test
    public void testHelpCommand(){
        CommandProcessor commandProcessor = new CommandProcessor(null);
        CommandData commandData = new CommandData("/help",null);
        Assert.assertEquals("Wrong message","""
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
        CommandProcessor commandProcessor = new CommandProcessor(fakeService);
        fakeService.saveJoke(new Joke("""
                — Заходит программист в лифт, а ему надо на 12—й этаж.
                — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                """));

        CommandData commandData = new CommandData("/joke",null);
        Assert.assertEquals("Invalid message",String.format("Анекдот №1%n")+"""
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
        CommandProcessor commandProcessor = new CommandProcessor(fakeService);

        fakeService.saveJoke(new Joke("""
                — Заходит программист в лифт, а ему надо на 12—й этаж.
                — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                """));

        CommandData commandData = new CommandData("/getJoke","0");
        Assert.assertEquals("Invalid message",String.format("Анекдот №1%n")+"""
                — Заходит программист в лифт, а ему надо на 12—й этаж.
                — Нажимает 1, потом 2 и начинает лихорадочно искать кнопку Enter.
                """,
                commandProcessor.runCommand(commandData));
    }
}
