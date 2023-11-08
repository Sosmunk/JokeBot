package org.bot;

import org.bot.dto.CommandData;
import org.bot.dto.ParserCommand;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Тест ParserCommand
 */
public class ParserCommandTest {
    // TODO Добавить валидацию commandData через Hibernate
    ParserCommand parserCommand = new ParserCommand();
        /**
         * Валидатор hibernate, оставил в качестве примера
         * <pre>
         *     Validator validator;
         *     @Before
         *     public void setUpValidator(){
         *         ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
         *         validator = validatorFactory.getValidator();
         *     }
         * </pre>
         */
    /**
     * Проверка на null
     */

    @Test
    public void parseMessageNotNull(){
        Assert.assertNotNull("Command is null!",parserCommand.parseMessage("/start"));
    }
    /**
     * Проверка команды /getJoke id
     */
    @Test
    public void parserMessageGetJoke(){
        CommandData commandDataExpected = new CommandData("/getJoke","15");
        CommandData commandDataActual = parserCommand.parseMessage("/getJoke 15");
        long arg = Long.parseLong(commandDataActual.getArgs());
        Assert.assertEquals("The command '/getJoke' do not match",commandDataExpected.getCommand(),commandDataActual.getCommand());
        Assert.assertEquals("Arguments don't match",commandDataExpected.getArgs(),commandDataActual.getArgs());
        Assert.assertTrue("Invalid arg", 0 <= arg && arg <= Long.MAX_VALUE);
    }
    /**
     * Проверка команд /start /help /joke
     */
    @Test
    public void parserMessageWithoutArgs(){
        List<String> commands = new ArrayList<>(Arrays.asList("/start","/help","/joke"));
        for (String command : commands) {
            CommandData commandDataExpected = new CommandData(command,null);
            CommandData commandDataActual = parserCommand.parseMessage(command);
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
        CommandData commandDataActual = parserCommand.parseMessage("/rate 15 5");
        String[] args = commandDataActual.getArgs().split(" ");
        Assert.assertEquals("The command '/rate' do not match",commandDataExpected.getCommand(),commandDataActual.getCommand());
        Assert.assertEquals("Arguments don't match",commandDataExpected.getArgs(),commandDataActual.getArgs());
        Assert.assertTrue("Invalid arg(id)",0 <= Long.parseLong(args[0]) && Long.parseLong(args[0]) <= Long.MAX_VALUE);
        Assert.assertTrue("Invalid arg(rate)",0 < Long.parseLong(args[1]) && Long.parseLong(args[1]) < 6);
    }

    /**
     * Тест команды /rate
     */
    @Test
    public void parserMessageCheckArgs(){
        CommandData commandDataActual = parserCommand.parseMessage("/getJoke 5");
        String[] args = commandDataActual.getArgs().split(" ");
        Assert.assertTrue("Invalid arguments",0 <= Long.parseLong(args[0]) &&  Long.parseLong(args[0]) <= Long.MAX_VALUE);
    }
}