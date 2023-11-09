package org.bot;

import org.bot.dto.CommandData;
import org.bot.dto.CommandParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void parseMessageNotNull(){
        Assert.assertNotNull("Command is null!", commandParser.parseMessage("/start"));
    }

    /**
     * Тест проверки количества аргументов команды /getJoke
     */
    @Test
    public void parserGetJokeCountArgs(){
        String[] args = commandParser.parseMessage("/getJoke 15").getArgs().split(" ");
        Assert.assertEquals("Invalid count arguments",1,args.length);
    }

    /**
     * Тест корректного ввода команды /getJoke
     */
    @Test
    public void parserGetJokeCorrectCommand(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = commandParser.parseMessage("/getJoke 15");
        Assert.assertEquals("Invalid command",commandDataExpected.getCommand(),commandDataActual.getCommand());
    }

    /**
     * Тест проверки корректности аргументов команды /getJoke
     */
    @Test
    public void parserGetJokeInvalidArgs(){
        long arg = Long.parseLong(commandParser.parseMessage("/getJoke -1").getArgs());
        Assert.assertFalse("Invalid arg", 0 <= arg);
    }

    /**
     * Проверка аргументов команды /getJoke на null
     */
    @Test
    public void parserGetJokeNoArgs(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = commandParser.parseMessage("/getJoke");
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

    /**
     * Тест команды /rate
     */
    @Test
    public void parserMessageRate(){
        CommandData commandDataExpected = new CommandData("/rate","15 5");
        CommandData commandDataActual = commandParser.parseMessage("/rate 15 5");
        String[] args = commandDataActual.getArgs().split(" ");
        Assert.assertEquals("The command '/rate' do not match",commandDataExpected.getCommand(),commandDataActual.getCommand());
        Assert.assertEquals("Arguments don't match",commandDataExpected.getArgs(),commandDataActual.getArgs());
        Assert.assertTrue("Invalid arg(id)",0 <= Long.parseLong(args[0]) && Long.parseLong(args[0]) <= Long.MAX_VALUE);
        Assert.assertTrue("Invalid arg(rate)",0 < Long.parseLong(args[1]) && Long.parseLong(args[1]) < 6);
    }
    @Test
    public void parserMessageCheckArgs(){
        CommandData commandDataActual = commandParser.parseMessage("/getJoke 5");
        String[] args = commandDataActual.getArgs().split(" ");
        Assert.assertTrue("Invalid arguments",0 <= Long.parseLong(args[0]) &&  Long.parseLong(args[0]) <= Long.MAX_VALUE);
    }
}