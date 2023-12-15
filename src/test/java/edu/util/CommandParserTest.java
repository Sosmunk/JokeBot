package edu.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Тесты CommandParser
 */
public class CommandParserTest {
    private final CommandParser commandParser = new CommandParser();
    /**
     * Тест корректного ввода команды /getJoke
     */
    @Test
    public void parserGetJokeCorrect(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = commandParser.parseMessage("/getJoke 15");

        Assert.assertEquals("Invalid command!",
                commandDataExpected,
                commandDataActual);
    }

    /**
     * Тест аргументов команды /getJoke на их отсутствие
     */
    @Test
    public void parserGetJokeNoArgs(){
        CommandData commandDataActual = commandParser.parseMessage("/getJoke");
        Assert.assertNull("Args is null!",
                commandDataActual.args());
    }
    /**
     * Тестирование парсера на возврат null значений в полях, при передаче пустой строки
     */
    @Test
    public void parseNoData() {
        CommandData commandData = commandParser.parseMessage("");
        Assert.assertNull(commandData.command());
        Assert.assertNull(commandData.args());
    }
}