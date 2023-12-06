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
     * Тестирование парсера на возврат null значений в полях, при передаче пустой строки
     */
    @Test
    public void parseNoData() {
        CommandData commandData = commandParser.parseMessage("");
        Assert.assertNull(commandData.getCommand());
        Assert.assertNull(commandData.getArgs());
    }
}