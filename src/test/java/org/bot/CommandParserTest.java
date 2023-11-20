package org.bot;

import org.bot.dto.CommandData;
import org.bot.dto.CommandParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Тесты CommandParser
 */
public class CommandParserTest {
    CommandParser commandParser = new CommandParser();
    /**
     * Тест корректного ввода команды /getJoke
     */
    @Test
    public void parserGetJokeCorrect(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = commandParser.parseMessage("/getJoke 15");
        Assert.assertEquals("Invalid command!",commandDataExpected,commandDataActual);
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
        Assert.assertEquals("Invalid command!",commandDataExpected,commandDataActual);
    }

    /**
     * Тест команды /rate <id> <stars 1-5> на отсутствие аругментов
     */
    @Test
    public void parserRateNoArgs(){
        CommandData commandDataActual = commandParser.parseMessage("/rate");
        Assert.assertNull("Args is null!",commandDataActual.getArgs());
    }
}