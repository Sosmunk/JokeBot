package org.bot.commands;

import org.bot.FakeService;
import org.junit.Assert;
import org.junit.Test;

public class CommandProcessorTest {
    private final FakeService jokeService = new FakeService();
    private final CommandProcessor commandProcessor = new CommandProcessor(jokeService);

    /**
     * Тестирование на null аргумента runCommand(command)
     */
    @Test
    public void testRunCommandWithNull() {
        Assert.assertEquals("Команда не найдена", commandProcessor.runCommand(null));
    }
}