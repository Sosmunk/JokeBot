package org.bot;

import org.bot.commands.CommandProcessor;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;
import org.bot.dto.CommandData;
import org.junit.Assert;
import org.junit.Test;

public class CommandTest {
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
        // TODO : Запустить докер и попробовать
        JokeService jokeService = new JokeServiceImpl();
        CommandProcessor commandProcessor = new CommandProcessor(jokeService);
        CommandData commandData = new CommandData("/joke",null);
        Assert.assertEquals("Wrong message","""
                Тестировщик заходит в бар и заказывает:
                                
                кружку пива,
                2 кружки пива,
                0 кружек пива,
                999999999 кружек пива,
                ящерицу в стакане,
                –1 кружку пива,
                qwertyuip кружек пива.
                                
                Первый реальный клиент заходит в бар и спрашивает, где туалет. Бар вспыхивает пламенем, все погибают.
                """,
                commandProcessor.runCommand(commandData));
    }

    /**
     * Тест команды /getJoke <id>
     */
    @Test
    public void testGetJokeCommand(){
        // TODO : Запустить докер и попробовать
        JokeService jokeService = new JokeServiceImpl();
        CommandProcessor commandProcessor = new CommandProcessor(jokeService);
        CommandData commandData = new CommandData("/getJoke","1");
        Assert.assertTrue("Incorrect input",commandData.getArgs().matches("[0-9]+"));
        Assert.assertTrue("Incorrect input",Integer.parseInt(commandData.getArgs()) > 0);
        Assert.assertEquals("Invalid message","""
                Тестировщик заходит в бар и заказывает:
                                
                кружку пива,
                2 кружки пива,
                0 кружек пива,
                999999999 кружек пива,
                ящерицу в стакане,
                –1 кружку пива,
                qwertyuip кружек пива.
                                
                Первый реальный клиент заходит в бар и спрашивает, где туалет. Бар вспыхивает пламенем, все погибают.
                """,
                commandProcessor.runCommand(commandData));
    }
}
