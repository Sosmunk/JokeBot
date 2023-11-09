package org.bot;

import org.bot.dto.CommandData;
import org.bot.dto.CommandParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Тест ParserCommand
 */
public class CommandParserTest {
    // TODO Добавить валидацию commandData через Hibernate
    CommandParser commandParser = new CommandParser();
    /**
     * Проверка на null
     */
    @Test
    public void parserMessageNotNull(){
        Assert.assertNotNull("Command is null!", commandParser.parseMessage("/start"));
    }

    /**
     * Тест корректного ввода команды /getJoke
     */
    @Test
    public void parserGetJokeCorrect(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = commandParser.parseMessage("/getJoke 15");
        Assert.assertTrue(commandDataExpected.equals(commandDataActual));
    }

    /**
     * Тест корректного ввода команды /getJoke
     */
    @Test
    public void parserGetJokeNoCommand(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = commandParser.parseMessage("/getJoke 15");
        Assert.assertEquals("Invalid command",commandDataExpected.getCommand(),commandDataActual.getCommand());
    }

    /**
     * Тест аргументов команды /getJoke на их отсутствие
     */
    @Test
    public void parserGetJokeNoArgs(){
        CommandData commandDataActual = commandParser.parseMessage("/getJoke");
        Assert.assertNull("Args is null!",commandDataActual.getArgs());
    }

    /**
     * Тест корректного ввода команды /rate <id> <stars 1-5>
     */
    @Test
    public void parserRateCorrect(){
        CommandData commandDataExpected = new CommandData("/rate","15 5");
        CommandData commandDataActual = commandParser.parseMessage("/rate 15 5");
        Assert.assertTrue(commandDataExpected.equals(commandDataActual));
    }

    /**
     * Тест корректного ввода команды /rate <id> <stars 1-5>
     */
    @Test
    public void parserRateNoCommand(){
        CommandData commandDataExpected = new CommandData("/rate","15 4");
        CommandData commandDataActual = commandParser.parseMessage("/rate 15 4");
        Assert.assertEquals("Invalid command",commandDataExpected.getCommand(),commandDataActual.getCommand());
    }

    /**
     * Тест команды /rate <id> <stars 1-5> на отсутствие аругментов
     */
    @Test
    public void parserRateNoArgs(){
        CommandData commandDataActual = commandParser.parseMessage("/rate");
        Assert.assertNull("Args is null!",commandDataActual.getArgs());
    }

    /**
     * Проверка команд /start /help /joke
     */
    @Test
    public void parserMessageWithoutArgs(){
        List<String> commands = new ArrayList<>(Arrays.asList("/start","/help","/joke"));
        for (String command : commands) {
            CommandData commandDataExpected = new CommandData(command,null);
            CommandData commandDataActual = commandParser.parseMessage(command);
            Assert.assertEquals("The commands do not match",commandDataExpected.getCommand(),commandDataActual.getCommand());
            Assert.assertEquals("Arguments don't match",commandDataExpected.getArgs(),commandDataActual.getArgs());
        }
    }
}